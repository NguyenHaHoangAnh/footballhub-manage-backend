package com.example.footballhub_manage_backend.service.impl;

import com.example.core.message.ResponseMsg;
import com.example.core.service.AbstractCRUDService;
import com.example.core.util.HttpRequestUtil;
import com.example.core.util.Utils;
import com.example.footballhub_manage_backend.dto.*;
import com.example.footballhub_manage_backend.mapper.TeamMapper;
import com.example.footballhub_manage_backend.model.Area;
import com.example.footballhub_manage_backend.model.Competition;
import com.example.footballhub_manage_backend.model.CompetitionTeamMap;
import com.example.footballhub_manage_backend.model.Team;
import com.example.footballhub_manage_backend.repository.AreaRepository;
import com.example.footballhub_manage_backend.repository.CompetitionRepository;
import com.example.footballhub_manage_backend.repository.CompetitionTeamMapRepository;
import com.example.footballhub_manage_backend.repository.TeamRepository;
import com.example.footballhub_manage_backend.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TeamServiceImpl extends AbstractCRUDService<Team, Integer, TeamRepository> implements TeamService {
    @Value("${link.url.football-data-api}")
    private String footballDataApi;

    @Value("${football-data-api.x-auth-token}")
    private String xAuthToken;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private CompetitionTeamMapRepository competitionTeamMapRepository;

    @Autowired
    private TeamMapper teamMapper;

    @Override
    public ResponseMsg<?> updateTeamManually(TeamRequestDto requestDto) throws Exception {
        try {
            List<String> competitionCodes = List.of("PL", "PD", "BL1", "SA", "FL1"); // premier league, la liga, bundesliga, serie a, league 1

            // db data: team
            List<Team> teams = this.findAll();

            Set<Integer> thirdPartySet = teams.stream()
                    .map(Team::getThirdPartyId)
                    .collect(Collectors.toSet());

            List<Team> allNewTeams = new ArrayList<>();
            for (String competitionCode: competitionCodes) {
                List<Team> newTeams = new ArrayList<>();
                String jsonResponse;
                String url = "/competitions/" + competitionCode + "/teams?season=" + requestDto.getSeason();
                try {
                    jsonResponse = HttpRequestUtil.sendGet(footballDataApi + url, null, xAuthToken, 60);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                TeamListDto teamListDto = Utils.getGson().fromJson(jsonResponse, TeamListDto.class);
                if (teamListDto == null) {
                    return ResponseMsg.newResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Không lấy được danh sách đội tại api bên thứ 3");
                }

                // compare db data w/ 3rd data: team
                for (TeamDto dto: teamListDto.getTeams()) {
                    // if team not existed -> create new
                    if (!thirdPartySet.contains(dto.getId())) {
                        Team newTeam = new Team();
                        newTeam.setName(dto.getName());
                        newTeam.setShortName(dto.getShortName());
                        newTeam.setTla(dto.getTla());
                        Area area = this.areaRepository.findByThirdPartyId(dto.getArea().getId());
                        if (area == null) {
                            log.error("[team] update manually: area null");
                            return ResponseMsg.new500ErrorResponse();
                        }
                        newTeam.setAreaId(area.getAreaId());
                        newTeam.setLogoUrl(dto.getCrest());
                        newTeam.setAddress(dto.getAddress());
                        newTeam.setWebsite(dto.getWebsite());
                        newTeam.setFounded(dto.getFounded());
                        newTeam.setClubColors(dto.getClubColors());
                        newTeam.setVenue(dto.getVenue());
                        newTeam.setThirdPartyId(dto.getId());

                        newTeams.add(newTeam);
                    }
                }

                if (!newTeams.isEmpty()) {
                    allNewTeams.addAll(newTeams);
                    if (!this.saveAllBeans(newTeams)) {
                        return ResponseMsg.new500ErrorResponse();
                    }
                }

                // Sau khi save, xử lý các competition mà mỗi đội đã tham gia
                // db data: competition_team_map
                List<CompetitionTeamMap> competitionTeamMaps = this.competitionTeamMapRepository.findAll();

                Set<String> thirdPartyIdCompetitionTeamMapSet = competitionTeamMaps.stream()
                        .map(item -> item.getCompetitionThirdPartyId() + "_" + item.getCompetitionThirdPartyId())
                        .collect(Collectors.toSet());

                List<CompetitionTeamMap> newCompetitionTeamMaps = new ArrayList<>();
                for (TeamDto dto: teamListDto.getTeams()) {
                    // duyệt từng competition mà team đó đang tham gia
                    for (CompetitionDto competitionDto: dto.getRunningCompetitions()) {
                        if (!thirdPartyIdCompetitionTeamMapSet.contains(competitionDto.getId() + "_" + dto.getId())) {
                            CompetitionTeamMap newCompetitionTeamMap = new CompetitionTeamMap();
                            Competition competition = this.competitionRepository.findByThirdPartyId(competitionDto.getId());
                            if (competition == null) {
//                                log.error("[team] update manually: competition null competitionName={}", competitionDto.getName());
//                                return ResponseMsg.new500ErrorResponse();
                                continue;
                            }
                            Team team = this.teamRepository.findByThirdPartyId(dto.getId());
                            if (team == null) {
                                log.error("[error] update manually: team null");
                                return ResponseMsg.new500ErrorResponse();
                            }
                            newCompetitionTeamMap.setCompetitionId(competition.getCompetitionId());
                            newCompetitionTeamMap.setTeamId(team.getTeamId());
                            newCompetitionTeamMap.setCompetitionThirdPartyId(competition.getThirdPartyId());
                            newCompetitionTeamMap.setTeamThirdPartyId(team.getThirdPartyId());

                            newCompetitionTeamMaps.add(newCompetitionTeamMap);
                        }
                    }
                }

                if (!newCompetitionTeamMaps.isEmpty()) {
                    List<CompetitionTeamMap> savedCompetitionTeamMaps = this.competitionTeamMapRepository.saveAll(newCompetitionTeamMaps);
                    if (savedCompetitionTeamMaps.isEmpty()) {
                        return ResponseMsg.new500ErrorResponse();
                    }
                }
            }

            return ResponseMsg.newOKResponse(allNewTeams);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public ResponseMsg<?> findByTeamId(Integer teamId) throws Exception {
        try {
            TeamResponseDto responseDto = this.teamMapper.findByTeamId(teamId);
            if (responseDto == null) {
                return ResponseMsg.new500ErrorResponse();
            }

            return ResponseMsg.newOKResponse(responseDto);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public ResponseMsg<?> findByCompetitionId(Integer competitionId) throws Exception {
        try {
            List<Team> teams = this.teamMapper.findByCompetitionId(competitionId);
            if (teams == null) {
                return ResponseMsg.new500ErrorResponse();
            }

            return ResponseMsg.newOKResponse(teams);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}

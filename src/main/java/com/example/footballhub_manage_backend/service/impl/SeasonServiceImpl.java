package com.example.footballhub_manage_backend.service.impl;

import com.example.core.message.ResponseMsg;
import com.example.core.service.AbstractCRUDService;
import com.example.core.util.HttpRequestUtil;
import com.example.core.util.Utils;
import com.example.footballhub_manage_backend.dto.CompetitionDto;
import com.example.footballhub_manage_backend.dto.SeasonDto;
import com.example.footballhub_manage_backend.dto.SeasonRequestDto;
import com.example.footballhub_manage_backend.dto.TeamListDto;
import com.example.footballhub_manage_backend.mapper.SeasonMapper;
import com.example.footballhub_manage_backend.model.Competition;
import com.example.footballhub_manage_backend.model.Season;
import com.example.footballhub_manage_backend.repository.CompetitionRepository;
import com.example.footballhub_manage_backend.repository.SeasonRepository;
import com.example.footballhub_manage_backend.service.SeasonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SeasonServiceImpl extends AbstractCRUDService<Season, Integer, SeasonRepository> implements SeasonService {
    @Value("${link.url.football-data-api}")
    private String footballDataApi;

    @Value("${football-data-api.x-auth-token}")
    private String xAuthToken;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private SeasonMapper seasonMapper;

    @Override
    public ResponseMsg<?> updateSeasonManually(SeasonRequestDto requestDto) throws Exception {
        try {
            // Tổng ko quá 10 api 1p
            List<String> competitionCodes = List.of("PL", "PD", "BL1", "SA", "FL1"); // premier league, la liga, bundesliga, serie a, league 1

            // db data
            List<Season> seasons = this.findAll();

//            Set<Integer> thirdPartySet = seasons.stream()
//                    .map(Season::getThirdPartyId)
//                    .collect(Collectors.toSet());

            Map<Integer, Season> thirdPartyMap = seasons.stream()
                    .collect(Collectors.toMap(
                            Season::getThirdPartyId,
                            season -> season
                    ));

            List<Season> newSeasons = new ArrayList<>();
            for(String competitionCode: competitionCodes) {
                String url = "/competitions/" + competitionCode + "/teams?season=" + requestDto.getSeason().toString(); // "/competitions/PL/teams?season=2025"
                // Lấy data qua api bên thứ 3
                try {
                    String jsonResponse = HttpRequestUtil.sendGet(footballDataApi + url, null, xAuthToken, 60);
                    TeamListDto teamListDto = Utils.getGson().fromJson(jsonResponse, TeamListDto.class);
                    if (teamListDto == null) {
                        return ResponseMsg.newResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Không lấy được danh sách mùa giải tại api bên thứ 3");
                    }

                    CompetitionDto competitionDto = teamListDto.getCompetition();
                    SeasonDto seasonDto = teamListDto.getSeason();

                    // Lấy competition từ db
                    Competition competition = this.competitionRepository.findByThirdPartyId(competitionDto.getId());
                    if (competition == null) {
                        log.error("[season] update manually: competition not found");
                        return ResponseMsg.new500ErrorResponse();
                    }

                    // Kiểm tra xem season đã tồn tại chưa qua thirdPartyId
                    // Nếu season chưa tồn tại -> tạo season mới lưu vào db
                    if (!thirdPartyMap.containsKey(seasonDto.getId())) {
                        Season newSeason = new Season();
                        newSeason.setName(requestDto.getSeason().toString() + " - " + competition.getName());
//                        newSeason.setCompetitionName(competitionDto.getName());
                        newSeason.setYear(requestDto.getSeason());
                        newSeason.setCompetitionId(competition.getCompetitionId());
                        newSeason.setStartDate(seasonDto.getStartDate());
                        newSeason.setEndDate(seasonDto.getEndDate());
                        newSeason.setCurrentMatchDay(seasonDto.getCurrentMatchday());
                        Integer winnerId = (seasonDto.getWinner() != null) ? seasonDto.getWinner().getId() : null;
                        newSeason.setWinnerId(winnerId);
                        newSeason.setThirdPartyId(seasonDto.getId());

                        newSeasons.add(newSeason);
                    } else {
                        // Nếu đã tồn tại thì kiểm tra và cập nhật currentMatchDay
                        // Nếu currentMatchDay khác thì mới cập nhật
                        if (seasonDto.getCurrentMatchday().equals(thirdPartyMap.get(seasonDto.getId()).getCurrentMatchDay())) continue;
                        Season currentSeason = this.seasonRepository.findByThirdPartyId(seasonDto.getId());
                        if (currentSeason == null) {
                            log.error("[season] update manually: current season not found");
                            continue;
                        }
                        currentSeason.setCurrentMatchDay(seasonDto.getCurrentMatchday());
                        this.seasonRepository.save(currentSeason);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (!newSeasons.isEmpty()) {
                if (this.saveAllBeans(newSeasons)) {
                    // update competition season id
                    for (Season season: newSeasons) {
                        Optional<Competition> optionalCompetition = this.competitionRepository.findById(season.getCompetitionId());
                        if (optionalCompetition.isEmpty()) {
                            log.error("[season] update manually: update competition failed");
                            return ResponseMsg.new500ErrorResponse();
                        }
                        Competition competition = optionalCompetition.get();
                        competition.setCurrentSeasonId(season.getSeasonId());
                        this.competitionRepository.save(competition);
                    }
                    return ResponseMsg.newOKResponse(newSeasons);
                } else {
                    return ResponseMsg.new500ErrorResponse();
                }
            }

            return ResponseMsg.newOKResponse(newSeasons);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public ResponseMsg<?> findBySeasonId(Integer seasonId) throws Exception {
        try {
            Season seasons = this.seasonMapper.findBySeasonId(seasonId);
            if (seasons == null) {
                return ResponseMsg.new500ErrorResponse();
            }

            return ResponseMsg.newOKResponse(seasons);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public ResponseMsg<?> findByCompetitionId(Integer competitionId) throws Exception {
        try {
            List<Season> seasons = this.seasonMapper.findByCompetitionId(competitionId);
            if (seasons == null) {
                return ResponseMsg.new500ErrorResponse();
            }

            return ResponseMsg.newOKResponse(seasons);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}

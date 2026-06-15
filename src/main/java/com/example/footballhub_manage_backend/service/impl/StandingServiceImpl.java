package com.example.footballhub_manage_backend.service.impl;

import com.example.core.message.ResponseMsg;
import com.example.core.service.AbstractCRUDService;
import com.example.footballhub_manage_backend.constant.Constant;
import com.example.footballhub_manage_backend.dto.*;
import com.example.footballhub_manage_backend.mapper.MatchMapper;
import com.example.footballhub_manage_backend.mapper.StandingMapper;
import com.example.footballhub_manage_backend.mapper.TeamMapper;
import com.example.footballhub_manage_backend.model.*;
import com.example.footballhub_manage_backend.repository.*;
import com.example.footballhub_manage_backend.service.StandingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class StandingServiceImpl extends AbstractCRUDService<Standing, Integer, StandingRepository> implements StandingService {
    @Value("${link.url.football-data-api}")
    private String footballDataApi;

    @Value("${football-data-api.x-auth-token}")
    private String xAuthToken;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private CompetitionTeamMapRepository competitionTeamMapRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private StandingRepository standingRepository;

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private MatchMapper matchMapper;

    @Autowired
    private StandingMapper standingMapper;

    @Override
    public ResponseMsg<?> updateStandingManually(StandingRequestDto requestDto) throws Exception {
        try {
            Optional<Competition> optionalCompetition = this.competitionRepository.findById(requestDto.getCompetitionId());
            if (optionalCompetition.isEmpty()) {
                log.error("[standing] update manually: competition not found");
                return ResponseMsg.new500ErrorResponse();
            }
            Competition competition = optionalCompetition.get();

            Optional<Season> optionalSeason = this.seasonRepository.findById(requestDto.getSeasonId());
            if (optionalSeason.isEmpty()) {
                log.error("[standing] update manually: season not found");
                return ResponseMsg.new500ErrorResponse();
            }
            Season season = optionalSeason.get();

            Optional<Area> optionalArea = this.areaRepository.findById(competition.getAreaId());
            if (optionalArea.isEmpty()) {
                log.error("[standing] update manually: area not found");
                return ResponseMsg.new500ErrorResponse();
            }
            Area area = optionalArea.get();

            List<Team> teams = this.teamMapper.findByCompetitionIdAndSeasonId(competition.getCompetitionId(), season.getSeasonId());
            if (teams == null || teams.isEmpty()) {
                log.error("[standing] update manually: teams not found");
                return ResponseMsg.new500ErrorResponse();
            }

            List<Standing> standings = new ArrayList<>();
            for (Team team: teams) {
                Standing standing = this.standingRepository.findByCompetitionIdAndSeasonIdAndTeamId(
                        competition.getCompetitionId(),
                        season.getSeasonId(),
                        team.getTeamId()
                );
                if (standing == null) standing = new Standing();
                standing.setAreaId(area.getAreaId());
                standing.setCompetitionId(competition.getCompetitionId());
                standing.setSeasonId(season.getSeasonId());
                standing.setTeamId(team.getTeamId());

                List<Match> matches = this.matchMapper.findByCompetitionIdAndSeasonIdAndTeamId(
                        competition.getCompetitionId(),
                        season.getSeasonId(),
                        team.getTeamId()
                );
                if (matches == null || matches.isEmpty()) {
                    log.error("[standing] update manually: matches not found teamId={}", team.getTeamId());
                    standings.add(standing);
                    continue;
                }

                List<String> formList = new ArrayList<>();
//                if (standing.getForm() == null || standing.getForm().isEmpty()) {
//                    formList = new ArrayList<>();
//                } else {
//                    formList = new ArrayList<>(Arrays.asList(standing.getForm().split(",")));
//                }
                Integer won = 0, lost = 0, draw = 0;
                Integer goalsFor = 0, goalsAgainst = 0;
                for (Match match: matches) {
                    if (match.getStatus().equals(Constant.MATCH_STATUS.FINISHED) || match.getStatus().equals(Constant.MATCH_STATUS.CANCELLED)) {
                        Integer playedGames = match.getMatchDay();
                        if (formList.size() == 5) {
                            formList.remove(0);
                        }
                        if (match.getWinnerId() == null) {
                            formList.add(Constant.FORM_STATUS.DRAW);
                            draw++;
                        } else if (team.getTeamId().equals(match.getWinnerId())) {
                            formList.add(Constant.FORM_STATUS.WON);
                            won++;
                        } else {
                            formList.add(Constant.FORM_STATUS.LOST);
                            lost++;
                        }
                        String form = formList.isEmpty() ? null : String.join(",", formList);
                        standing.setForm(form);
                        standing.setPlayedGames(playedGames);
                        standing.setWon(won);
                        standing.setDraw(draw);
                        standing.setLost(lost);

                        Integer points = Constant.POINT_STATUS.WON * won
                                + Constant.POINT_STATUS.DRAW * draw
                                + Constant.POINT_STATUS.LOST * lost;
                        standing.setPoints(points);

                        boolean isHome = team.getTeamId().equals(match.getHomeTeamId());
                        goalsFor += isHome ? match.getScoreHome() : match.getScoreAway();
                        goalsAgainst += isHome ? match.getScoreAway() : match.getScoreHome();
                        Integer goalsDifference = goalsFor - goalsAgainst;
                        standing.setGoalsFor(goalsFor);
                        standing.setGoalsAgainst(goalsAgainst);
                        standing.setGoalsDifference(goalsDifference);
                    } else {
                        break;
                    }
                }

                standings.add(standing);
            }

            if (this.saveAllBeans(standings)) {
                return ResponseMsg.newOKResponse(standings);
            } else {
                return ResponseMsg.new500ErrorResponse();
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public ResponseMsg<?> findByCompetitionIdAndSeasonId(StandingRequestDto requestDto) throws Exception {
        try {
            List<StandingResponseDto> standings = this.standingMapper.findByCompetitionIdAndSeasonId(requestDto.getCompetitionId(), requestDto.getSeasonId());
            return ResponseMsg.newOKResponse(standings);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}

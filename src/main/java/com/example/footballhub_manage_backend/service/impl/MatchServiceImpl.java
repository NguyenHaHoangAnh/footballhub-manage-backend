package com.example.footballhub_manage_backend.service.impl;

import com.example.core.message.ResponseMsg;
import com.example.core.service.AbstractCRUDService;
import com.example.core.util.HttpRequestUtil;
import com.example.core.util.Utils;
import com.example.footballhub_manage_backend.dto.MatchDto;
import com.example.footballhub_manage_backend.dto.MatchListDto;
import com.example.footballhub_manage_backend.dto.MatchRequestDto;
import com.example.footballhub_manage_backend.dto.MatchResponseDto;
import com.example.footballhub_manage_backend.mapper.MatchMapper;
import com.example.footballhub_manage_backend.model.*;
import com.example.footballhub_manage_backend.repository.*;
import com.example.footballhub_manage_backend.service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MatchServiceImpl extends AbstractCRUDService<Match, Integer, MatchRepository> implements MatchService {
    @Value("${link.url.football-data-api}")
    private String footballDataApi;

    @Value("${football-data-api.x-auth-token}")
    private String xAuthToken;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MatchMapper matchMapper;

    public ResponseMsg<?> updateManually(MatchRequestDto requestDto) throws Exception {
        try {
            Optional<Competition> optionalCompetition = this.competitionRepository.findById(requestDto.getCompetitionId());
            if (optionalCompetition.isEmpty()) {
                log.error("[team] update manually: competition not found");
                return ResponseMsg.new500ErrorResponse();
            }
            Competition competition = optionalCompetition.get();

            Optional<Season> optionalSeason = this.seasonRepository.findById(requestDto.getSeasonId());
            if (optionalSeason.isEmpty()) {
                log.error("[team] update manually: season not found");
                return ResponseMsg.new500ErrorResponse();
            }
            Season season = optionalSeason.get();

            String url = "/competitions/" + competition.getCode() + "/matches?season=" + season.getYear() + "&matchday=" + requestDto.getMatchDay();
            String jsonResponse = HttpRequestUtil.sendGet(footballDataApi + url, null, xAuthToken, 60);
            MatchListDto matchListDto = Utils.getGson().fromJson(jsonResponse, MatchListDto.class);
            if (matchListDto == null) {
                return ResponseMsg.newResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Không lấy được danh sách trận tại api bên thứ 3");
            }

            // db data
            List<Match> matches = this.matchRepository.findByCompetitionIdAndSeasonIdAndMatchDay(
                    requestDto.getCompetitionId(),
                    requestDto.getSeasonId(),
                    requestDto.getMatchDay()
            );

//            Set<Integer> thirdPartySet = matches.stream()
//                    .map(Match::getThirdPartyId)
//                    .collect(Collectors.toSet());

            Map<Integer, Match> thirdPartyMap = matches.stream()
                    .collect(Collectors.toMap(
                            Match::getThirdPartyId,
                            match -> match
                    ));

            // compare db data w/ 3rd party data
            List<Match> newMatches = new ArrayList<>();
            for (MatchDto dto: matchListDto.getMatches()) {
                // Kiểm tra xem match đã tồn tại chưa
                if (!thirdPartyMap.containsKey(dto.getId())) {
                    Match newMatch = new Match();
                    Area area = this.areaRepository.findByThirdPartyId(dto.getArea().getId());
                    if (area == null) {
                        log.error("[match] update manually: area not found");
                        return ResponseMsg.new500ErrorResponse();
                    }
                    newMatch.setAreaId(area.getAreaId());
                    newMatch.setCompetitionId(competition.getCompetitionId());
                    newMatch.setSeasonId(season.getSeasonId());
                    newMatch.setStartDate(dto.getUtcDate());
                    newMatch.setStatus(dto.getStatus());
                    newMatch.setMatchDay(dto.getMatchday());
                    Team homeTeam = this.teamRepository.findByThirdPartyId(dto.getHomeTeam().getId());
                    if (homeTeam == null) {
                        log.error("[team] update manually: home team null");
                        return ResponseMsg.new500ErrorResponse();
                    }
                    newMatch.setHomeTeamId(homeTeam.getTeamId());
                    Team awayTeam = this.teamRepository.findByThirdPartyId(dto.getAwayTeam().getId());
                    if (awayTeam == null) {
                        log.error("[team] update manually: away team null");
                        return ResponseMsg.new500ErrorResponse();
                    }
                    newMatch.setAwayTeamId(awayTeam.getTeamId());
                    newMatch.setScoreHome(dto.getScore().getFullTime().getHome());
                    newMatch.setScoreAway(dto.getScore().getFullTime().getAway());
                    if (dto.getScore().getWinner().equals("HOME_TEAM")) {
                        newMatch.setWinnerId(homeTeam.getTeamId());
                    } else if (dto.getScore().getWinner().equals("AWAY_TEAM")) {
                        newMatch.setWinnerId(awayTeam.getTeamId());
                    } else {
                        newMatch.setWinnerId(null);
                    }
                    newMatch.setThirdPartyId(dto.getId());

                    newMatches.add(newMatch);
                } else {
                    // Nếu đã tồn tại -> so sánh status -> cập nhật kết quả
                    if (dto.getStatus().equals(thirdPartyMap.get(dto.getId()).getStatus())) continue;
                    Match currentMatch = this.matchRepository.findByThirdPartyId(dto.getId());
                    if (currentMatch == null) {
                        log.error("[match] update manually: match not found");
                        return ResponseMsg.new500ErrorResponse();
                    }

                    // Cập nhật kết quả, trạng thái
                    currentMatch.setStatus(dto.getStatus());
                    Team homeTeam = this.teamRepository.findByThirdPartyId(dto.getHomeTeam().getId());
                    if (homeTeam == null) {
                        log.error("[team] update manually: home team null, matchId={}", currentMatch.getMatchId());
                        return ResponseMsg.new500ErrorResponse();
                    }
                    currentMatch.setHomeTeamId(homeTeam.getTeamId());
                    Team awayTeam = this.teamRepository.findByThirdPartyId(dto.getAwayTeam().getId());
                    if (awayTeam == null) {
                        log.error("[team] update manually: away team null, matchId={}", currentMatch.getMatchId());
                        return ResponseMsg.new500ErrorResponse();
                    }
                    currentMatch.setAwayTeamId(awayTeam.getTeamId());
                    currentMatch.setScoreHome(dto.getScore().getFullTime().getHome());
                    currentMatch.setScoreAway(dto.getScore().getFullTime().getAway());
                    if (dto.getScore().getWinner().equals("HOME_TEAM")) {
                        currentMatch.setWinnerId(homeTeam.getTeamId());
                    } else if (dto.getScore().getWinner().equals("AWAY_TEAM")) {
                        currentMatch.setWinnerId(awayTeam.getTeamId());
                    } else {
                        currentMatch.setWinnerId(null);
                    }

                    this.matchRepository.save(currentMatch);
                }
            }

            if (!newMatches.isEmpty()) {
                if (this.saveAllBeans(newMatches)) {
                    return ResponseMsg.newOKResponse(newMatches);
                } else {
                    return ResponseMsg.new500ErrorResponse();
                }
            }

            return ResponseMsg.newOKResponse(newMatches);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public ResponseMsg<?> findByMatchId(Integer matchId) throws Exception {
        try {
            MatchResponseDto matches = this.matchMapper.findById(matchId);
            return ResponseMsg.newOKResponse(matches);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public ResponseMsg<?> findByTeamId(Integer teamId, Pageable pageable) throws Exception {
        try {
            List<Match> matches = this.matchMapper.findByTeamId(
                    teamId,
                    pageable.getPageNumber(),
                    pageable.getPageSize()
            );
            return ResponseMsg.newOKResponse(matches);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}

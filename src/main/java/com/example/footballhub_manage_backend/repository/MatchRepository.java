package com.example.footballhub_manage_backend.repository;

import com.example.core.repository.BaseRepo;
import com.example.footballhub_manage_backend.model.Match;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends BaseRepo<Match, Integer> {
    List<Match> findByCompetitionIdAndSeasonIdAndMatchDay(Integer competitionId, Integer SeasonId, Integer matchDay);

    List<Match> findByCompetitionIdAndSeasonId(Integer competitionId, Integer seasonId);

    Match findByThirdPartyId(Integer thirdPartyId);
}

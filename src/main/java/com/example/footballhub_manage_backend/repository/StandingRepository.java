package com.example.footballhub_manage_backend.repository;

import com.example.core.repository.BaseRepo;
import com.example.footballhub_manage_backend.model.Standing;
import org.springframework.stereotype.Repository;

@Repository
public interface StandingRepository extends BaseRepo<Standing, Integer> {
    Standing findByCompetitionIdAndSeasonIdAndTeamId(Integer competitionId, Integer seasonId, Integer teamId);
}

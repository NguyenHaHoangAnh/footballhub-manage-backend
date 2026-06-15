package com.example.footballhub_manage_backend.repository;

import com.example.core.repository.BaseRepo;
import com.example.footballhub_manage_backend.model.Season;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeasonRepository extends BaseRepo<Season, Integer> {
    Season findByThirdPartyId(Integer thirdPartyId);

    List<Season> findByCompetitionId(Integer competitionId);
}

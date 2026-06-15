package com.example.footballhub_manage_backend.repository;

import com.example.core.repository.BaseRepo;
import com.example.footballhub_manage_backend.model.Competition;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitionRepository extends BaseRepo<Competition, Integer> {
    Competition findByCode(String code);

    Competition findByThirdPartyId(Integer thirdPartyId);
}

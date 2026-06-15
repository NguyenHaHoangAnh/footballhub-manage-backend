package com.example.footballhub_manage_backend.repository;

import com.example.core.repository.BaseRepo;
import com.example.footballhub_manage_backend.model.Team;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends BaseRepo<Team, Integer> {
    Team findByThirdPartyId(Integer thirdPartyId);
}

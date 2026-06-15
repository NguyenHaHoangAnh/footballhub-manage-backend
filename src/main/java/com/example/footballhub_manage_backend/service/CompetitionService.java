package com.example.footballhub_manage_backend.service;

import com.example.core.message.ResponseMsg;

public interface CompetitionService {
    ResponseMsg<?> updateCompetitionManually() throws Exception;

    ResponseMsg<?> findByCompetitionId(Integer competitionId) throws Exception;
}

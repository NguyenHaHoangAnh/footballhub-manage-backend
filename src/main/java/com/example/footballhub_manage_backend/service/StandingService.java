package com.example.footballhub_manage_backend.service;

import com.example.core.message.ResponseMsg;
import com.example.footballhub_manage_backend.dto.StandingRequestDto;

public interface StandingService {
    ResponseMsg<?> updateStandingManually(StandingRequestDto requestDto) throws Exception;

    ResponseMsg<?> findByCompetitionIdAndSeasonId(StandingRequestDto requestDto) throws Exception;
}

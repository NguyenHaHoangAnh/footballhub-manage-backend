package com.example.footballhub_manage_backend.service;

import com.example.core.message.ResponseMsg;
import com.example.footballhub_manage_backend.dto.MatchRequestDto;
import org.springframework.data.domain.Pageable;

public interface MatchService {
    ResponseMsg<?> updateManually(MatchRequestDto requestDto) throws Exception;

    ResponseMsg<?> findByMatchId(Integer matchId) throws Exception;

    ResponseMsg<?> findByTeamId(Integer teamId, Pageable pageable) throws Exception;
}

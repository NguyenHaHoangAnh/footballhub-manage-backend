package com.example.footballhub_manage_backend.service;

import com.example.core.message.ResponseMsg;
import com.example.footballhub_manage_backend.dto.TeamRequestDto;

public interface TeamService {
    ResponseMsg<?> updateTeamManually(TeamRequestDto requestDto) throws Exception;

    ResponseMsg<?> findByTeamId(Integer teamId) throws Exception;

    ResponseMsg<?> findByCompetitionId(Integer competitionId) throws Exception;
}

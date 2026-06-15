package com.example.footballhub_manage_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchResponseDto {
    private Integer matchId;
    private Integer areaId;
    private String areaName;
    private Integer competitionId;
    private Integer seasonId;
    private Integer matchDay;
    private Date startDate;
    private String status;
    private Integer homeTeamId;
    private Integer awayTeamId;
    private Integer scoreHome;
    private Integer scoreAway;
    private Integer winnerId;
}

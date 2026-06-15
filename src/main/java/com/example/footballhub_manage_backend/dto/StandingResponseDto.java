package com.example.footballhub_manage_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandingResponseDto {
    private Integer standingId;
    private Integer areaId;
    private Integer competitionId;
    private Integer seasonId;
    private Integer teamId;
    private Integer position;
    private String teamName;
    private String teamLogoUrl;
    private Integer playedGames;
    private String form;
    private Integer won;
    private Integer draw;
    private Integer lost;
    private Integer points;
    private Integer goalsFor;
    private Integer goalsAgainst;
    private Integer goalsDifference;
    private Date createdAt;
    private String createdBy;
    private Date updatedAt;
    private String updatedBy;
}

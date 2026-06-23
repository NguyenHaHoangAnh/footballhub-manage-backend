package com.example.footballhub_manage_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponseDto {
    private Integer teamId;
    private String name;
    private String shortName;
    private String tla;
    private Integer areaId;
    private String areaName;
    private String logoUrl;
    private String address;
    private String website;
    private Integer founded;
    private String clubColors;
    private String venue;
    private Integer currentCompetitionId;
    private Integer currentSeasonId;
}

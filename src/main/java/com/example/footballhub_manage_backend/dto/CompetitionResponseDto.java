package com.example.footballhub_manage_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionResponseDto {
    private Integer competitionId;
    private Integer areaId;
    private String areaName;
    private String name;
    private String code;
    private String type;
    private String logoUrl;
    private String currentSeasonId;
    private String seasonName;
}

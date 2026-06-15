package com.example.footballhub_manage_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionDto {
    private Integer id;

    private String name;

    private String code;

    private AreaDto area;

    private String type;

    private String emblem;

    private String plan;

    private SeasonDto currentSeason;
}

package com.example.footballhub_manage_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDto {
    private Integer id;

    private AreaDto area;

    private String name;

    private String shortName;

    private String tla;

    private String crest;

    private String address;

    private String website;

    private Integer founded;

    private String clubColors;

    private String venue;

    private List<CompetitionDto> runningCompetitions;
}

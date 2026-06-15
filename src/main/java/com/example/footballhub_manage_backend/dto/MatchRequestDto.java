package com.example.footballhub_manage_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchRequestDto {
    private Integer competitionId;

//    private String competitionCode;

    private Integer seasonId;

//    private Integer season;

    private Integer matchDay;
}

package com.example.footballhub_manage_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeasonResponseDto {
    private Integer seasonId;
    private String name;
    //    private String competitionName;
    private Integer competitionId;
    private Integer year;
    private Date startDate;
    private Date endDate;
    private Integer currentMatchDay;
    private Integer winnerId;
}

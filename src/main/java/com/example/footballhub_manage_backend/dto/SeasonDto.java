package com.example.footballhub_manage_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeasonDto {
    private Integer id;

    private Date startDate;

    private Date endDate;

    private Integer currentMatchday;

    private TeamDto winner;
}

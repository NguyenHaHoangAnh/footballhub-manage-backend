package com.example.footballhub_manage_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDto {
    private AreaDto area;

    private CompetitionDto competition;

    private SeasonDto season;

    private Integer id;

    private Date utcDate;

    private String status;

    private Integer matchday;

    private TeamDto homeTeam;

    private TeamDto awayTeam;

    private ResultDto score;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResultDto {
        private String winner;

        private ScoreDto fullTime;

        private ScoreDto halfTime;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ScoreDto {
            private Integer home;

            private Integer away;
        }
    }
}

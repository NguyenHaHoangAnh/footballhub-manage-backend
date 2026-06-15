package com.example.footballhub_manage_backend.model;

import com.example.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "standing")
public class Standing extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "standing_generator")
    @SequenceGenerator(name = "standing_generator", sequenceName = "standing_seq", allocationSize = 1)
    @Column(name = "standing_id")
    private Integer standingId;

    @Column(name = "area_id")
    private Integer areaId;

    @Column(name = "competition_id")
    private Integer competitionId;

    @Column(name = "season_id")
    private Integer seasonId;

    @Column(name = "team_id")
    private Integer teamId;

    @Column(name = "played_games")
    private Integer playedGames;

    @Column(name = "form")
    private String form;

    @Column(name = "won")
    private Integer won;

    @Column(name = "draw")
    private Integer draw;

    @Column(name = "lost")
    private Integer lost;

    @Column(name = "points")
    private Integer points;

    @Column(name = "goals_for")
    private Integer goalsFor;

    @Column(name = "goals_against")
    private Integer goalsAgainst;

    @Column(name = "goals_difference")
    private Integer goalsDifference;
}

package com.example.footballhub_manage_backend.model;

import com.example.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "match")
public class Match extends BaseEntity {
    @Id
    @GeneratedValue(generator = "match_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "match_generator", sequenceName = "match_seq", allocationSize = 1)
    @Column(name = "match_id")
    private Integer matchId;

    @Column(name = "area_id")
    private Integer areaId;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", insertable = false, updatable = false)
    private Area area;

    @Column(name = "competition_id")
    private Integer competitionId;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id", insertable = false, updatable = false)
    private Competition competition;

    @Column(name = "season_id")
    private Integer seasonId;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", insertable = false, updatable = false)
    private Season season;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "status")
    private String status;

    @Column(name = "match_day")
    private Integer matchDay;

    @Column(name = "home_team_id")
    private Integer homeTeamId;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id", insertable = false, updatable = false)
    private Team homeTeam;

    @Column(name = "away_team_id")
    private Integer awayTeamId;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id", insertable = false, updatable = false)
    private Team awayTeam;

    @Column(name = "score_home")
    private Integer scoreHome;

    @Column(name = "score_away")
    private Integer scoreAway;

    @Column(name = "winner_id")
    private Integer winnerId;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id", insertable = false, updatable = false)
    private Team winner;

    @Column(name = "third_party_id")
    private Integer thirdPartyId;
}

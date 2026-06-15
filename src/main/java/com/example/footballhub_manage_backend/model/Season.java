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
@Table(name = "season")
public class Season extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "season_generator")
    @SequenceGenerator(name = "season_generator", sequenceName = "season_seq", allocationSize = 1)
    @Column(name = "season_id")
    private Integer seasonId;

    @Column(name = "name")
    private String name;

//    @Column(name = "competition_name")
//    private String competitionName;

    @Column(name = "competition_id")
    private Integer competitionId;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id", insertable = false, updatable = false)
    private Competition competition;

    @Column(name = "year")
    private Integer year;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "current_match_day")
    private Integer currentMatchDay;

    @Column(name = "winner_id")
    private Integer winnerId;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id", insertable = false, updatable = false)
    private Team winner;

    @Column(name = "third_party_id")
    private Integer thirdPartyId;
}

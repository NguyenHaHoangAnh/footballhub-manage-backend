package com.example.footballhub_manage_backend.model;

import com.example.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "competition")
public class Competition extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "competition_generator")
    @SequenceGenerator(name = "competition_generator", sequenceName = "competition_seq", allocationSize = 1)
    @Column(name = "competition_id")
    private Integer competitionId;

    @Column(name = "area_id")
    private Integer areaId;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", insertable = false, updatable = false)
    private Area area;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "type")
    private String type;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "current_season_id")
    private Integer currentSeasonId;

    @Column(name = "third_party_id")
    private Integer thirdPartyId;
}

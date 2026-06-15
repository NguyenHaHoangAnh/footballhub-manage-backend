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
@Table(name = "team")
public class Team extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_generator")
    @SequenceGenerator(name = "team_generator", sequenceName = "team_seq", allocationSize = 1)
    @Column(name = "team_id")
    private Integer teamId;

    @Column(name = "name")
    private String name;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "tla")
    private String tla;

    @Column(name = "area_id")
    private Integer areaId;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", insertable = false, updatable = false)
    private Area area;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "address")
    private String address;

    @Column(name = "website")
    private String website;

    @Column(name = "founded")
    private Integer founded;

    @Column(name = "club_colors")
    private String clubColors;

    @Column(name = "venue")
    private String venue;

    @Column(name = "third_party_id")
    private Integer thirdPartyId;
}

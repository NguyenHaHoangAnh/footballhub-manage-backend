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
@Table(name = "competition_team_map")
public class CompetitionTeamMap extends BaseEntity {
    @Id
    @GeneratedValue(generator = "competition_team_map_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "competition_team_map_generator", sequenceName = "competition_team_map_seq", allocationSize = 1)
    @Column(name = "competition_team_map_id")
    private Integer competitionTeamMapId;

    @Column(name = "competition_id")
    private Integer competitionId;

    @Column(name = "team_id")
    private Integer teamId;

    @Column(name = "competition_third_party_id")
    private Integer competitionThirdPartyId;

    @Column(name = "team_third_party_id")
    private Integer teamThirdPartyId;
}

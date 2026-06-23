package com.example.footballhub_manage_backend.mapper;

import com.example.footballhub_manage_backend.dto.TeamResponseDto;
import com.example.footballhub_manage_backend.model.Team;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TeamMapper {
    @Select("""
            <script>
                select
                    t.team_id as teamId,
                    t.name as name,
                    t.short_name as shortName,
                    t.tla as tla,
                    t.area_id as areaId,
                    a.name as areaName,
                    t.logo_url as logoUrl,
                    t.address as address,
                    t.website as website,
                    t.founded as founded,
                    t.club_colors as clubColors,
                    t.venue as venue,
                    c.competition_id as currentCompetitionId,
                    s.season_id as currentSeasonId,
                    t.created_at as createdAt,
                    t.created_by as createdBy,
                    t.updated_at as updatedAt,
                    t.updated_by as updatedBy
                from football.team t
                join football.area a on a.area_id = t.area_id
                join football.competition_team_map ctm on ctm.team_id = t.team_id
                join football.competition c on c.competition_id = ctm.competition_id
                join football.season s on s.season_id = (
                    select s2.season_id
                    from football.season s2
                    where
                        s2.competition_id = c.competition_id
                    order by s2.year desc
                    limit 1
                )
                where
                    t.team_id = #{teamId}
                    and c.type = 'LEAGUE'
            </script>
            """)
    TeamResponseDto findByTeamId(@Param("teamId") Integer teamId);

    @Select("""
            <script>
                select
                    t.team_id as teamId,
                    t.name as name,
                    t.short_name as shortName,
                    t.tla as tla,
                    t.area_id as areaId,
                    t.logo_url as logoUrl,
                    t.address as address,
                    t.website as website,
                    t.founded as founded,
                    t.club_colors as clubColors,
                    t.venue as venue,
                    t.created_at as createdAt,
                    t.created_by as createdBy,
                    t.updated_at as updatedAt,
                    t.updated_by as updatedBy
                from football.team t
                join football.competition_team_map ctm on ctm.team_id = t.team_id
                join football.competition c on c.competition_id = ctm.competition_id
                where
                    ctm.competition_id = #{competitionId}
                    and c.current_season_id = #{seasonId}
            </script>
            """)
    List<Team> findByCompetitionIdAndSeasonId(
            @Param("competitionId") Integer competitionId,
            @Param("seasonId") Integer seasonId
    );

    @Select("""
            <script>
                select
                    t.team_id as teamId,
                    t.name as name,
                    t.short_name as shortName,
                    t.tla as tla,
                    t.area_id as areaId,
                    t.logo_url as logoUrl,
                    t.address as address,
                    t.website as website,
                    t.founded as founded,
                    t.club_colors as clubColors,
                    t.venue as venue,
                    t.created_at as createdAt,
                    t.created_by as createdBy,
                    t.updated_at as updatedAt,
                    t.updated_by as updatedBy
                from football.team t
                join football.competition_team_map ctm on ctm.team_id = t.team_id
                where
                    ctm.competition_id = #{competitionId}
            </script>
            """)
    List<Team> findByCompetitionId(
            @Param("competitionId") Integer competitionId
    );
}

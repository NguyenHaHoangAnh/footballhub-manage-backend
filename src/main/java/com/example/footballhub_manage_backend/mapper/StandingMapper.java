package com.example.footballhub_manage_backend.mapper;

import com.example.footballhub_manage_backend.dto.StandingResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StandingMapper {
    @Select("""
            <script>
                select
                	row_number() over (
                		order by
                			s.points desc,
                			s.goals_difference desc,
                			s.goals_for desc,
                			s.goals_against asc,
                			t.name asc
                	) as position,
                	s.standing_id as standingId,
                	s.team_id as teamId,
                	t.name as teamName,
                	t.logo_url as teamLogoUrl,
                	s.played_games as playedGames,
                	s.area_id as areaId,
                	s.competition_id as competitionId,
                	s.season_id as seasonId,
                	s.won as won,
                	s.draw as draw,
                	s.lost as lost,
                	s.goals_for as goalsFor,
                	s.goals_against as goalsAgainst,
                	s.goals_difference as goalsDifference,
                	s.points as points,
                	s.form as form,
                	s.created_at as createdAt,
                	s.created_by as createdBy,
                	s.updated_at as updatedAt,
                	s.updated_by as updatedBy
                from football.standing s
                join team t on t.team_id = s.team_id
                where
                	s.competition_id = #{competitionId}
                	and s.season_id = #{seasonId}
                order by
                	position
            </script>
            """)
    List<StandingResponseDto> findByCompetitionIdAndSeasonId(
            @Param("competitionId") Integer competitionId,
            @Param("seasonId") Integer seasonId
    );
}

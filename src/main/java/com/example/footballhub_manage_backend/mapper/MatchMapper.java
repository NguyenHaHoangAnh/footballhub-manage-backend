package com.example.footballhub_manage_backend.mapper;

import com.example.footballhub_manage_backend.dto.MatchResponseDto;
import com.example.footballhub_manage_backend.model.Match;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MatchMapper {
    @Select("""
            <script>
                select
                    m.match_id as matchId,
                    m.match_day as matchDay,
                    m.status as status,
                    m.home_team_id as homeTeamId,
                    m.away_team_id as awayTeamId,
                    m.score_home as scoreHome,
                    m.score_away as scoreAway,
                    m.winner_id as winnerId
                from football.match m
                where
                    m.competition_id = #{competitionId}
                    and m.season_id = #{seasonId}
                    and (
                        m.home_team_id = #{teamId}
                        or m.away_team_id = #{teamId}
                    )
                    and (
                        m.status = 'FINISHED'
                        or m.status = 'CANCELLED'
                    )
                order by
                    m.match_day asc
            </script>
            """)
    List<Match> findByCompetitionIdAndSeasonIdAndTeamId(
            @Param("competitionId") Integer competitionId,
            @Param("seasonId") Integer seasonId,
            @Param("teamId") Integer teamId
    );

    @Select("""
            <script>
                select
                    m.match_id as matchId,
                    m.area_id as areaId,
                    a.name as areaName,
                    m.competition_id as competitionId,
                    m.season_id as seasonId,
                    m.match_day as matchDay,
                    m.start_date as startDate,
                    m.status as status,
                    m.home_team_id as homeTeamId,
                    m.away_team_id as awayTeamId,
                    m.score_home as scoreHome,
                    m.score_away as scoreAway,
                    m.winner_id as winnerId
                from football.match m
                join football.area a on a.area_id = m.area_id
                where
                    m.match_id = #{matchId}
            </script>
            """)
    MatchResponseDto findById(@Param("matchId") Integer matchId);

    @Select("""
            <script>
                select
                    m.match_id as matchId,
                    m.match_day as matchDay,
                    m.start_date as startDate,
                    m.status as status,
                    m.home_team_id as homeTeamId,
                    m.away_team_id as awayTeamId,
                    m.score_home as scoreHome,
                    m.score_away as scoreAway,
                    m.winner_id as winnerId
                from football.match m
                join
                where
                    m.home_team_id = #{teamId}
                    or m.away_team_id = #{teamId}
                order by
                    m.match_day asc
            </script>
            """)
    List<Match> findByTeamId(
            @Param("teamId") Integer teamId,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit
    );
}

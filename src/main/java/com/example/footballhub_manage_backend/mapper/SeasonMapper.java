package com.example.footballhub_manage_backend.mapper;

import com.example.footballhub_manage_backend.model.Season;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SeasonMapper {
    @Select("""
        <script>
            select
               s.season_id as seasonId,
               s.name as name,
               s.competition_id as competitionId,
               s.year as year,
               s.start_date as startDate,
               s.end_date as endDate,
               s.current_match_day as currentMatchDay,
               s.winner_id as winnerId
            from football.season s
            where
                s.season_id = #{seasonId}
        </script>""")
    Season findBySeasonId(@Param("seasonId") Integer seasonId);

    @Select("""
        <script>
            select
               s.season_id as seasonId,
               s.name as name,
               s.competition_id as competitionId,
               s.year as year,
               s.start_date as startDate,
               s.end_date as endDate,
               s.current_match_day as currentMatchDay,
               s.winner_id as winnerId
            from football.season s
            where
                s.competition_id = #{competitionId}
        </script>""")
    List<Season> findByCompetitionId(@Param("competitionId") Integer competitionId);
}

package com.example.footballhub_manage_backend.mapper;

import com.example.footballhub_manage_backend.dto.CompetitionResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CompetitionMapper {
    @Select("""
        <script>
            select
               c.competition_id as competitionId,
               c.area_id as areaId,
               a.name as areaName,
               c.name as name,
               c.code as code,
               c.type as type,
               c.logo_url as logoUrl,
               c.current_season_id as currentSeasonId,
               s.name as seasonName
            from football.competition c
            join football.area a on a.area_id = c.area_id
            join football.season s on s.season_id = c.current_season_id
            where
                c.competition_id = #{competitionId}
        </script>""")
    CompetitionResponseDto findByCompetitionId(@Param("competitionId") Integer competitionId);
}

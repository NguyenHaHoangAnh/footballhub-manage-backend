package com.example.footballhub_manage_backend.mapper;

import com.example.footballhub_manage_backend.model.Area;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AreaMapper {
    @Select("""
        <script>
            select
               a.area_id as areaId,
               a.name as name,
               a.country_code as countryCode,
               a.flag_url as flagUrl,
               a.parent_area_id as parentAreaId,
               a.parent_area as parentArea,
               a.third_party_id as thirdPartyId,
               a.created_at as createdAt,
               a.created_by as createdBy,
               a.updated_at as updatedAt
            from football.area a
            where a.area_id in (
               select
                   a2.parent_area_id
               from area a2
            )
        </script>""")
    List<Area> findParentAreas();
}

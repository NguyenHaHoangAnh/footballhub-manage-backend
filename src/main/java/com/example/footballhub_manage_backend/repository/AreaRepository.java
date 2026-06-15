package com.example.footballhub_manage_backend.repository;

import com.example.core.repository.BaseRepo;
import com.example.footballhub_manage_backend.model.Area;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends BaseRepo<Area, Integer> {
    Area findByName(String name);

    Area findByCountryCode(String countryCode);

    Area findByThirdPartyId(Integer thirdPartyId);
}

package com.example.footballhub_manage_backend.service.impl;

import com.example.core.message.ResponseMsg;
import com.example.core.service.AbstractCRUDService;
import com.example.core.util.HttpRequestUtil;
import com.example.core.util.Utils;
import com.example.footballhub_manage_backend.dto.AreaDto;
import com.example.footballhub_manage_backend.dto.AreaListDto;
import com.example.footballhub_manage_backend.mapper.AreaMapper;
import com.example.footballhub_manage_backend.model.Area;
import com.example.footballhub_manage_backend.repository.AreaRepository;
import com.example.footballhub_manage_backend.service.AreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AreaServiceImpl extends AbstractCRUDService<Area, Integer, AreaRepository> implements AreaService {
    @Value("${link.url.football-data-api}")
    private String footballDataApi;

    @Value("${football-data-api.x-auth-token}")
    private String xAuthToken;

    @Autowired
    private AreaMapper areaMapper;

    @Autowired
    private AreaRepository areaRepository;

    @Override
    public ResponseMsg<?> updateAreaManually() throws Exception {
        try {
            // Lấy data qua api bên thứ 3
            String jsonResponse = HttpRequestUtil.sendGet(footballDataApi + "/areas", null, xAuthToken, 60);
            AreaListDto areaListDto = Utils.getGson().fromJson(jsonResponse, AreaListDto.class);
            if (areaListDto == null) {
                return ResponseMsg.newResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Không lấy được danh sách khu vực tại api bên thứ 3");
            }

            // Lấy data trong db
            List<Area> areas = this.findAll();

            // Tạo Map: id (bên thứ 3) -> index: do id của bên thứ 3 != id do db tạo ra -> set parentAreaId = index
            HashMap<Integer, Integer> idMap = new HashMap<>();
            for (int i = 0; i < areaListDto.getCount(); i++) {
                AreaDto areaDto = areaListDto.getAreas().get(i);
                if (idMap.isEmpty() || !idMap.containsKey(areaDto.getId())) {
                    idMap.put(areaDto.getId(), i + 1);
                }
            }

            // Tạo Set chứa các third_party_id đã tồn tại trong DB (để so sánh nhanh)
            Set<Integer> thirdPartyIdSet = areas.stream()
                    .map(Area::getThirdPartyId)
                    .collect(Collectors.toSet());

            // Lọc lấy các data ko có trong db
            List<Area> newAreas = areaListDto.getAreas().stream()
                    .filter(dto -> !thirdPartyIdSet.contains(dto.getId()))
                    .map(dto -> {
                        Area newArea = new Area();
                        newArea.setName(dto.getName());
                        newArea.setCountryCode(dto.getCountryCode());
                        newArea.setFlagUrl(dto.getFlag());
                        newArea.setParentAreaId(idMap.get(dto.getParentAreaId()));
                        newArea.setParentArea(dto.getParentArea());
                        newArea.setThirdPartyId(dto.getId());

                        return newArea;
                    })
                    .collect(Collectors.toList());

            // Lưu vào db
            if (!newAreas.isEmpty()) {
                if (this.saveAllBeans(newAreas)) {
                    return ResponseMsg.newOKResponse(newAreas);
                } else {
                    return ResponseMsg.new500ErrorResponse();
                }
            }

            return ResponseMsg.newOKResponse(newAreas);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public ResponseMsg<?> findParentAreas() throws Exception {
        try {
            List<Area> parentAreas = this.areaMapper.findParentAreas();
            if (parentAreas == null || parentAreas.isEmpty()) {
                return ResponseMsg.new500ErrorResponse();
            }
            return ResponseMsg.newOKResponse(parentAreas);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public ResponseMsg<?> createArea(Area newArea) throws Exception {
        try {
            // Check payload
            if (newArea == null) {
                log.error("[area] create: payload null");
                return ResponseMsg.newResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Dữ liệu null");
            }

            if (newArea.getName() == null || newArea.getName().isEmpty()
                || newArea.getCountryCode() == null || newArea.getCountryCode().isEmpty()) {
                log.error("[area] create: empty name or countryCode");
                return ResponseMsg.newResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Tên và Tên viết tắt khu vực không được để trống");
            }

            // Check exist
            Area existedArea = this.areaRepository.findByName(newArea.getName());
            if (existedArea != null) {
                log.error("[area] create: name existed name={}", existedArea.getName());
                return ResponseMsg.newResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Tên khu vực đã tồn tại");
            }

            existedArea = this.areaRepository.findByCountryCode(newArea.getCountryCode());
            if (existedArea != null) {
                log.error("[area] create: countryCode existed countryCode={}", existedArea.getCountryCode());
                return ResponseMsg.newResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Tên viết tắt khu vực đã tồn tại");
            }

            // Check parentArea
            List<Area> parentAreas = this.areaMapper.findParentAreas();
            // Set parentAreaId
            Set<Integer> parentAreaSet = parentAreas.stream()
                    .map(Area::getAreaId)
                    .collect(Collectors.toSet());

            if (!parentAreaSet.contains(newArea.getParentAreaId())) {
                log.error("[area] create: parentAreaId invalid parentAreaId={}", newArea.getParentAreaId());
                return ResponseMsg.newResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Khu vực cha không hợp lệ");
            }

            return ResponseMsg.newOKResponse(super.saveBean(newArea));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public ResponseMsg<?> updateArea(Integer areaId, Area newArea) throws Exception {
        try {
            // Check payload
            if (newArea == null) {
                log.error("[area] update: payload null");
                return ResponseMsg.newResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Dữ liệu null");
            }

            if (newArea.getName() == null || newArea.getName().isEmpty()
                || newArea.getCountryCode() == null || newArea.getCountryCode().isEmpty()) {
                log.error("[area] update: empty name or countryCode");
                return ResponseMsg.newResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Tên và Tên viết tắt khu vực không được để trống");
            }

            // Check exist
            Optional<Area> optionalCurrentArea = this.areaRepository.findById(areaId);
            if (optionalCurrentArea.isEmpty()) {
                log.error("[area] update: area not existed areaId={}", areaId);
                return ResponseMsg.newResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Khu vực không tồn tại");
            }

            // Check parentArea
            List<Area> parentAreas = this.areaMapper.findParentAreas();
            // Set parentAreaId
            Set<Integer> parentAreaSet = parentAreas.stream()
                    .map(Area::getAreaId)
                    .collect(Collectors.toSet());

            if (!parentAreaSet.contains(newArea.getParentAreaId())) {
                log.error("[area] update: parentAreaId invalid parentAreaId={}", newArea.getParentAreaId());
                return ResponseMsg.newResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Khu vực cha không hợp lệ");
            }

            // Merge
            Area currentArea = optionalCurrentArea.get();
            currentArea.setName(newArea.getName());
            currentArea.setCountryCode(newArea.getCountryCode());
            currentArea.setFlagUrl(newArea.getFlagUrl());
            currentArea.setParentAreaId(newArea.getParentAreaId());
            currentArea.setParentArea(newArea.getParentArea());

            return ResponseMsg.newOKResponse(super.saveBean(currentArea));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}

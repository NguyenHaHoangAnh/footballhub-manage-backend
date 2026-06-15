package com.example.footballhub_manage_backend.service.impl;

import com.example.core.message.ResponseMsg;
import com.example.core.service.AbstractCRUDService;
import com.example.core.util.HttpRequestUtil;
import com.example.core.util.Utils;
import com.example.footballhub_manage_backend.dto.CompetitionListDto;
import com.example.footballhub_manage_backend.dto.CompetitionResponseDto;
import com.example.footballhub_manage_backend.mapper.CompetitionMapper;
import com.example.footballhub_manage_backend.model.Area;
import com.example.footballhub_manage_backend.model.Competition;
import com.example.footballhub_manage_backend.model.Season;
import com.example.footballhub_manage_backend.repository.AreaRepository;
import com.example.footballhub_manage_backend.repository.CompetitionRepository;
import com.example.footballhub_manage_backend.repository.SeasonRepository;
import com.example.footballhub_manage_backend.service.CompetitionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CompetitionServiceImpl extends AbstractCRUDService<Competition, Integer, CompetitionRepository> implements CompetitionService {
    @Value("${link.url.football-data-api}")
    private String footballDataApi;

    @Value("${football-data-api.x-auth-token}")
    private String xAuthToken;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private CompetitionMapper competitionMapper;

    @Override
    public ResponseMsg<?> updateCompetitionManually() throws Exception {
        try {
            // Lấy dữ liệu các giải Anh, TBN, Đức, ý, Pháp
            String jsonResponse = HttpRequestUtil.sendGet(footballDataApi + "/competitions?areas=2072,2224,2088,2114,2081", null, xAuthToken, 60);
            CompetitionListDto competitionListDto = Utils.getGson().fromJson(jsonResponse, CompetitionListDto.class);
            if (competitionListDto == null) {
                return ResponseMsg.newResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Không lấy được danh sách giải tại api bên thứ 3");
            }

            List<String> competitionCodes = List.of("PL", "PD", "BL1", "SA", "FL1"); // premier league, la liga, bundesliga, serie a, league 1

            // data from db
            List<Competition> competitions = this.findAll();

            Set<Integer> thirdPartyIdSet = competitions.stream()
                    .map(Competition::getThirdPartyId)
                    .collect(Collectors.toSet());

            // compare data from db w/ 3rd data -> add to db if not existed
            List<Competition> newCompetitions = competitionListDto.getCompetitions().stream()
                    .filter(dto -> competitionCodes.contains(dto.getCode()))
                    .filter(dto -> !thirdPartyIdSet.contains(dto.getId()))
                    .map(dto -> {
                        Competition newCompetition = new Competition();
                        Area area = this.areaRepository.findByThirdPartyId(dto.getArea().getId());
                        newCompetition.setAreaId(area.getAreaId());
                        newCompetition.setName(dto.getName());
                        newCompetition.setCode(dto.getCode());
                        newCompetition.setType(dto.getType());
                        newCompetition.setLogoUrl(dto.getEmblem());
                        Season season = this.seasonRepository.findByThirdPartyId(dto.getCurrentSeason().getId());
                        newCompetition.setCurrentSeasonId(season.getSeasonId());
                        newCompetition.setThirdPartyId(dto.getId());

                        return newCompetition;
                    })
                    .collect(Collectors.toList());

            if (!newCompetitions.isEmpty()) {
                if (this.saveAllBeans(newCompetitions)) {
                    return ResponseMsg.newOKResponse(newCompetitions);
                } else {
                    return ResponseMsg.new500ErrorResponse();
                }
            }

            return ResponseMsg.newOKResponse(newCompetitions);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public ResponseMsg<?> findByCompetitionId(Integer competitionId) throws Exception {
        try {
            CompetitionResponseDto responseDto = this.competitionMapper.findByCompetitionId(competitionId);
            if (responseDto == null) {
                return ResponseMsg.new500ErrorResponse();
            }

            return ResponseMsg.newOKResponse(responseDto);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}

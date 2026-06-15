package com.example.footballhub_manage_backend.controller;

import com.example.core.controller.BaseController;
import com.example.core.message.ResponseMsg;
import com.example.core.model.Filter;
import com.example.core.specification.CriteriaParser;
import com.example.core.specification.GenericSpecificationsBuilder;
import com.example.footballhub_manage_backend.constant.Constant;
import com.example.footballhub_manage_backend.dto.SeasonRequestDto;
import com.example.footballhub_manage_backend.model.Season;
import com.example.footballhub_manage_backend.repository.SeasonRepository;
import com.example.footballhub_manage_backend.service.SeasonService;
import com.example.footballhub_manage_backend.service.impl.SeasonServiceImpl;
import com.example.footballhub_manage_backend.specification.SeasonSpecification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.ApiService.PREFIX)
public class SeasonController extends BaseController<Season, Integer, SeasonRepository, SeasonServiceImpl> {
    @Autowired
    private SeasonService seasonService;

    @CrossOrigin("/**")
    @RequestMapping(value = "/seasons/updateManually", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> updateSeasonManually(@RequestBody SeasonRequestDto requestDto) throws Exception {
        return this.seasonService.updateSeasonManually(requestDto);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/seasons", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> findAllSeason(@RequestParam(value = "filters", required = false) String filtersJson, Pageable pageable) throws Exception {
        if (filtersJson == null || filtersJson.isEmpty()) {
            return super.findAll(pageable);
        }

        // parse json to Filters[]
        ObjectMapper mapper = new ObjectMapper();
        Filter[] filters = mapper.readValue(filtersJson, Filter[].class);

        CriteriaParser parser = new CriteriaParser();
        GenericSpecificationsBuilder<Season> builder = new GenericSpecificationsBuilder<>();
        Specification<Season> spec = builder.build(parser.parse(filters), SeasonSpecification::new);
        return super.findAll(spec, pageable);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/seasons/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> findSeasonById(@PathVariable("id") Integer id) throws Exception {
        return this.seasonService.findBySeasonId(id);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/seasons/competition/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> findSeasonByCompetitionId(@PathVariable("id") Integer id) throws Exception {
        return this.seasonService.findByCompetitionId(id);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/seasons", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> createSeason(@RequestBody Season season) throws Exception {
        return super.create(season);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/seasons/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> updateSeason(@PathVariable("id") Integer id, @RequestBody Season season) throws Exception {
        return super.update(id, season);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/seasons/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> deleteSeason(@PathVariable("id") Integer id) throws Exception {
        return super.delete(id);
    }

    @Override
    public void merge(Season newBean, Season currentBean) throws Exception {
        currentBean.setName(newBean.getName());
        currentBean.setCompetitionId(newBean.getCompetitionId());
        currentBean.setStartDate(newBean.getStartDate());
        currentBean.setEndDate(newBean.getEndDate());
        currentBean.setCurrentMatchDay(newBean.getCurrentMatchDay());
        currentBean.setWinnerId(newBean.getWinnerId());
//        currentBean.setThirdPartyId(newBean.getThirdPartyId());
    }
}

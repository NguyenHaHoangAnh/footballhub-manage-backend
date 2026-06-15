package com.example.footballhub_manage_backend.controller;

import com.example.core.controller.BaseController;
import com.example.core.message.ResponseMsg;
import com.example.core.model.Filter;
import com.example.core.specification.CriteriaParser;
import com.example.core.specification.GenericSpecificationsBuilder;
import com.example.footballhub_manage_backend.constant.Constant;
import com.example.footballhub_manage_backend.dto.MatchRequestDto;
import com.example.footballhub_manage_backend.model.Match;
import com.example.footballhub_manage_backend.repository.MatchRepository;
import com.example.footballhub_manage_backend.service.MatchService;
import com.example.footballhub_manage_backend.service.impl.MatchServiceImpl;
import com.example.footballhub_manage_backend.specification.MatchSpecification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.ApiService.PREFIX)
public class MatchController extends BaseController<Match, Integer, MatchRepository, MatchServiceImpl> {
    @Autowired
    MatchService matchService;

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/matches/updateManually", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> updateManually(@RequestBody MatchRequestDto requestDto) throws Exception {
        return this.matchService.updateManually(requestDto);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/matches", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> findAllMatch(@RequestParam(value = "filters", required = false) String filtersJson, Pageable pageable) throws Exception {
        if (filtersJson == null || filtersJson.isEmpty()) {
            return super.findAll(pageable);
        }

        // parse json to Filters[]
        ObjectMapper mapper = new ObjectMapper();
        Filter[] filters = mapper.readValue(filtersJson, Filter[].class);

        CriteriaParser parser = new CriteriaParser();
        GenericSpecificationsBuilder<Match> builder = new GenericSpecificationsBuilder<>();
        Specification<Match> spec = builder.build(parser.parse(filters), MatchSpecification::new);
        return super.findAll(spec, pageable);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/matches/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> findMatchById(@PathVariable("id") Integer id) throws Exception {
        return this.matchService.findByMatchId(id);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/matches", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> createMatch(@RequestBody Match match) throws Exception {
        return super.create(match);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/matches/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> updateMatch(@PathVariable("id") Integer id, @RequestBody Match match) throws Exception {
        return super.update(id, match);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/matches/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> deleteMatch(@PathVariable("id") Integer id) throws Exception {
        return super.delete(id);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/matches/team/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> findMatchByTeamId(@PathVariable("id") Integer id, Pageable pageable) throws Exception {
        return this.matchService.findByTeamId(id, pageable);
    }

    @Override
    public void merge(Match newBean, Match currentBean) throws Exception {
        currentBean.setAreaId(newBean.getAreaId());
        currentBean.setCompetitionId(newBean.getCompetitionId());
        currentBean.setSeasonId(newBean.getSeasonId());
        currentBean.setStartDate(newBean.getStartDate());
        currentBean.setStatus(newBean.getStatus());
        currentBean.setMatchDay(newBean.getMatchDay());
        currentBean.setHomeTeamId(newBean.getHomeTeamId());
        currentBean.setAwayTeamId(newBean.getAwayTeamId());
        currentBean.setScoreHome(newBean.getScoreHome());
        currentBean.setScoreAway(newBean.getScoreAway());
        currentBean.setWinnerId(newBean.getWinnerId());
    }
}

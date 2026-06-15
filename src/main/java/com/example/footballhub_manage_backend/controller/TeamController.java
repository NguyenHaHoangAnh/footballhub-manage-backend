package com.example.footballhub_manage_backend.controller;

import com.example.core.controller.BaseController;
import com.example.core.message.ResponseMsg;
import com.example.core.model.Filter;
import com.example.core.specification.CriteriaParser;
import com.example.core.specification.GenericSpecificationsBuilder;
import com.example.footballhub_manage_backend.constant.Constant;
import com.example.footballhub_manage_backend.dto.TeamRequestDto;
import com.example.footballhub_manage_backend.model.Team;
import com.example.footballhub_manage_backend.repository.TeamRepository;
import com.example.footballhub_manage_backend.service.TeamService;
import com.example.footballhub_manage_backend.service.impl.TeamServiceImpl;
import com.example.footballhub_manage_backend.specification.TeamSpecification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.ApiService.PREFIX)
public class TeamController extends BaseController<Team, Integer, TeamRepository, TeamServiceImpl> {
    @Autowired
    private TeamService teamService;

    @CrossOrigin("/**")
    @RequestMapping(value = "/teams/updateManually", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> updateTeamManually(@RequestBody TeamRequestDto requestDto) throws Exception {
        return this.teamService.updateTeamManually(requestDto);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/teams", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> findAllTeam(@RequestParam(value = "filters", required = false) String filtersJson, Pageable pageable) throws Exception {
        if (filtersJson == null || filtersJson.isEmpty()) {
            return super.findAll(pageable);
        }

        // parse json to Filters[]
        ObjectMapper mapper = new ObjectMapper();
        Filter[] filters = mapper.readValue(filtersJson, Filter[].class);

        CriteriaParser parser = new CriteriaParser();
        GenericSpecificationsBuilder<Team> builder = new GenericSpecificationsBuilder<>();
        Specification<Team> spec = builder.build(parser.parse(filters), TeamSpecification::new);
        return super.findAll(spec, pageable);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/teams/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> findTeamById(@PathVariable("id") Integer id) throws Exception {
        return this.teamService.findByTeamId(id);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/teams", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> createTeam(@RequestBody Team team) throws Exception {
        return super.create(team);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/teams/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> updateTeam(@PathVariable("id") Integer id, @RequestBody Team team) throws Exception {
        return super.update(id, team);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "teams/competition/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> findTeamByCompetitionId(@PathVariable("id") Integer id) throws Exception {
        return this.teamService.findByCompetitionId(id);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/teams/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> deleteTeam(@PathVariable("id") Integer id) throws Exception {
        return super.delete(id);
    }

    @Override
    public void merge(Team newBean, Team currentBean) throws Exception {
        currentBean.setName(newBean.getName());
        currentBean.setShortName(newBean.getShortName());
        currentBean.setTla(newBean.getTla());
        currentBean.setAreaId(newBean.getAreaId());
        currentBean.setLogoUrl(newBean.getLogoUrl());
        currentBean.setAddress(newBean.getAddress());
        currentBean.setWebsite(newBean.getWebsite());
        currentBean.setFounded(newBean.getFounded());
        currentBean.setClubColors(newBean.getClubColors());
        currentBean.setVenue(newBean.getVenue());
    }
}

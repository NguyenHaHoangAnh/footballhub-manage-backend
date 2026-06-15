package com.example.footballhub_manage_backend.controller;

import com.example.core.controller.BaseController;
import com.example.core.message.ResponseMsg;
import com.example.core.model.Filter;
import com.example.core.specification.CriteriaParser;
import com.example.core.specification.GenericSpecificationsBuilder;
import com.example.footballhub_manage_backend.constant.Constant;
import com.example.footballhub_manage_backend.model.Competition;
import com.example.footballhub_manage_backend.repository.CompetitionRepository;
import com.example.footballhub_manage_backend.service.CompetitionService;
import com.example.footballhub_manage_backend.service.impl.CompetitionServiceImpl;
import com.example.footballhub_manage_backend.specification.CompetitionSpecification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.ApiService.PREFIX)
public class CompetitionController extends BaseController<Competition, Integer, CompetitionRepository, CompetitionServiceImpl> {
    @Autowired
    private CompetitionService competitionService;

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/competitions/updateManually", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> updateCompetitionManually() throws Exception {
        return this.competitionService.updateCompetitionManually();
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/competitions", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> findAllCompetition(@RequestParam(value = "filters", required = false) String filtersJson, Pageable pageable) throws Exception {
        if (filtersJson == null || filtersJson.isEmpty()) {
            return super.findAll(pageable);
        }

        // parse json to Filters[]
        ObjectMapper mapper = new ObjectMapper();
        Filter[] filters = mapper.readValue(filtersJson, Filter[].class);

        CriteriaParser parser = new CriteriaParser();
        GenericSpecificationsBuilder<Competition> builder = new GenericSpecificationsBuilder<>();
        Specification<Competition> spec = builder.build(parser.parse(filters), CompetitionSpecification::new);
        return super.findAll(spec, pageable);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/competitions/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> findCompetitionById(@PathVariable("id") Integer id) throws Exception {
        return this.competitionService.findByCompetitionId(id);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/competitions", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> createCompetition(@RequestBody Competition competition) throws Exception {
        return super.create(competition);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/competitions/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> updateCompetition(@PathVariable("id") Integer id, @RequestBody Competition competition) throws Exception {
        return super.update(id, competition);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/competitions/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> deleteCompetition(@PathVariable("id") Integer id) throws Exception {
        return super.delete(id);
    }

    @Override
    public void merge(Competition newBean, Competition currentBean) throws Exception {
        currentBean.setAreaId(newBean.getAreaId());
        currentBean.setName(newBean.getName());
        currentBean.setCode(newBean.getCode());
        currentBean.setType(newBean.getType());
        currentBean.setLogoUrl(newBean.getLogoUrl());
        currentBean.setCurrentSeasonId(newBean.getCurrentSeasonId());
    }
}

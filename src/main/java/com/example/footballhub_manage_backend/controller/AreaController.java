package com.example.footballhub_manage_backend.controller;

import com.example.core.controller.BaseController;
import com.example.core.message.ResponseMsg;
import com.example.core.model.Filter;
import com.example.core.specification.CriteriaParser;
import com.example.core.specification.GenericSpecificationsBuilder;
import com.example.footballhub_manage_backend.constant.Constant;
import com.example.footballhub_manage_backend.model.Area;
import com.example.footballhub_manage_backend.repository.AreaRepository;
import com.example.footballhub_manage_backend.service.AreaService;
import com.example.footballhub_manage_backend.service.impl.AreaServiceImpl;
import com.example.footballhub_manage_backend.specification.AreaSpecification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.ApiService.PREFIX)
public class AreaController extends BaseController<Area, Integer, AreaRepository, AreaServiceImpl> {
    @Autowired
    AreaService areaService;

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/areas/updateManually", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> updateAreaManually() throws Exception {
        return this.areaService.updateAreaManually();
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/areas", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> findAllArea(@RequestParam(value = "filters", required = false) String filtersJson, Pageable pageable) throws Exception {
        if (filtersJson == null || filtersJson.isEmpty()) {
            return super.findAll(pageable);
        }

        // parse json to Filters[]
        ObjectMapper mapper = new ObjectMapper();
        Filter[] filters = mapper.readValue(filtersJson, Filter[].class);

        CriteriaParser parser = new CriteriaParser();
        GenericSpecificationsBuilder<Area> builder = new GenericSpecificationsBuilder<>();
        Specification<Area> spec = builder.build(parser.parse(filters), AreaSpecification::new);
        return super.findAll(spec, pageable);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/areas/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> findAreaById(@PathVariable("id") Integer id) throws Exception {
        return super.findById(id);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/areas/parentAreas", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> findParentAreas() throws Exception {
        return this.areaService.findParentAreas();
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/areas", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> createArea(@RequestBody Area area) throws Exception {
        return this.areaService.createArea(area);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/areas/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> updateArea(@PathVariable("id") Integer id, @RequestBody Area area) throws Exception {
        return this.areaService.updateArea(id, area);
    }

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/areas/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> deleteArea(@PathVariable("id") Integer id) throws Exception {
        return super.delete(id);
    }

    @Override
    public void merge(Area newBean, Area currentBean) throws Exception {
        currentBean.setName(newBean.getName());
        currentBean.setCountryCode(newBean.getCountryCode());
        currentBean.setFlagUrl(newBean.getFlagUrl());
        currentBean.setParentAreaId(newBean.getParentAreaId());
        currentBean.setParentArea(newBean.getParentArea());
    }
}

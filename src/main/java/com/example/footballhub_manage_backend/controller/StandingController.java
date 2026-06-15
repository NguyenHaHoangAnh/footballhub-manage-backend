package com.example.footballhub_manage_backend.controller;

import com.example.core.controller.BaseController;
import com.example.core.message.ResponseMsg;
import com.example.footballhub_manage_backend.constant.Constant;
import com.example.footballhub_manage_backend.dto.StandingRequestDto;
import com.example.footballhub_manage_backend.model.Standing;
import com.example.footballhub_manage_backend.repository.StandingRepository;
import com.example.footballhub_manage_backend.service.StandingService;
import com.example.footballhub_manage_backend.service.impl.StandingServiceImpl;
//import com.example.footballhub_admin_backend.specification.StandingSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.ApiService.PREFIX)
public class StandingController extends BaseController<Standing, Integer, StandingRepository, StandingServiceImpl> {
    @Autowired
    private StandingService standingService;

    @CrossOrigin("/**")
    @RequestMapping(value = "/standings/updateManually", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> updateStandingManually(@RequestBody StandingRequestDto requestDto) throws Exception {
        return this.standingService.updateStandingManually(requestDto);
    }

    @CrossOrigin("/**")
    @RequestMapping(value = "/standings", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg<?> findStandingByCompetitionIdAndSeasonId(@RequestBody StandingRequestDto requestDto) throws Exception {
        return this.standingService.findByCompetitionIdAndSeasonId(requestDto);
    }

//    @CrossOrigin(origins = "/**")
//    @RequestMapping(value = "/standings", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    @ResponseBody
//    public ResponseMsg<?> findAllStanding(@RequestParam(value = "filters", required = false) String filtersJson, Pageable pageable) throws Exception {
//        if (filtersJson == null || filtersJson.isEmpty()) {
//            return super.findAll(pageable);
//        }
//
//        // parse json to Filters[]
//        ObjectMapper mapper = new ObjectMapper();
//        Filter[] filters = mapper.readValue(filtersJson, Filter[].class);
//
//        CriteriaParser parser = new CriteriaParser();
//        GenericSpecificationsBuilder<Standing> builder = new GenericSpecificationsBuilder<>();
//        Specification<Standing> spec = builder.build(parser.parse(filters), StandingSpecification::new);
//        return super.findAll(spec, pageable);
//    }
//
//    @CrossOrigin(origins = "/**")
//    @RequestMapping(value = "/standings/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    @ResponseBody
//    public ResponseMsg<?> findStandingById(@PathVariable("id") Integer id) throws Exception {
//        return this.teamService.findByStandingId(id);
//    }
//
//    @CrossOrigin(origins = "/**")
//    @RequestMapping(value = "/standings", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    @ResponseBody
//    public ResponseMsg<?> createStanding(@RequestBody Standing standing) throws Exception {
//        return super.create(standing);
//    }
//
//    @CrossOrigin(origins = "/**")
//    @RequestMapping(value = "/standings/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    @ResponseBody
//    public ResponseMsg<?> updateStanding(@PathVariable("id") Integer id, @RequestBody Standing standing) throws Exception {
//        return super.update(id, standing);
//    }
//
//    @CrossOrigin(origins = "/**")
//    @RequestMapping(value = "/standings/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    @ResponseBody
//    public ResponseMsg<?> deleteStanding(@PathVariable("id") Integer id) throws Exception {
//        return super.delete(id);
//    }

    @Override
    public void merge(Standing newBean, Standing currentBean) throws Exception {

    }
}

package com.spring.boot.application.controller;

import com.spring.boot.application.common.AbstractBaseController;
import com.spring.boot.application.common.utils.RestAPIResponse;
import com.spring.boot.application.controller.model.request.company.JobRequest;
import com.spring.boot.application.controller.model.response.PagingResponse;
import com.spring.boot.application.services.job.JobService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPath.JOB_APIs)
public class JobController extends AbstractBaseController {
    final private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @Operation(summary = "addJob")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> addJob(
            @RequestBody JobRequest jobRequest
            ){
        return responseUtil.successResponse(jobRequest);
    }


    @Operation(summary = "getJobsByCompanyId")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getJobs(
            @RequestParam String companyId,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ){
        return responseUtil.successResponse(
                new PagingResponse(jobService.getPageJobByCompanyId(companyId, pageNumber, pageSize),
                        jobService.getJobsByCompanyId(companyId)));
    }


    @Operation(summary = "getJobsByCompanyId")
    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getAllJobs(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ){
        return responseUtil.successResponse(
                new PagingResponse(jobService.getPageAllJobs(pageNumber, pageSize),
                        jobService.getAllJobs()));
    }


    @Operation(summary = "getJobsByCompanyId")
    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> searchJobs(
            @RequestParam String searchKey
            ){
        return responseUtil.successResponse(jobService.searchJobs(searchKey));
    }
}

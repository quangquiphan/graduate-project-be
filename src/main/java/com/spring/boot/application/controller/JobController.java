package com.spring.boot.application.controller;

import com.spring.boot.application.common.AbstractBaseController;
import com.spring.boot.application.common.auth.AuthorizeValidator;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.common.utils.RestAPIResponse;
import com.spring.boot.application.controller.model.request.company.JobRequest;
import com.spring.boot.application.controller.model.response.PagingResponse;
import com.spring.boot.application.services.job.JobService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(ApiPath.JOB_APIs)
public class JobController extends AbstractBaseController {
    final private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @Operation(summary = "addJob")
    @AuthorizeValidator({UserRole.COMPANY_ADMIN, UserRole.COMPANY_ADMIN_MEMBER})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> addJob(
            @RequestBody JobRequest jobRequest
            ){
        return responseUtil.successResponse(jobService.addJob(jobRequest));
    }

    @Operation(summary = "updateJob")
    @AuthorizeValidator({UserRole.COMPANY_ADMIN, UserRole.COMPANY_ADMIN_MEMBER})
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> updateJob(
            @PathVariable String id,
            @RequestBody JobRequest jobRequest
    ){
        return responseUtil.successResponse(jobService.updateJob(id, jobRequest));
    }

    @Operation(summary = "getJob")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getJob(
            @PathVariable String id
    ) throws IOException {
        return responseUtil.successResponse(jobService.getJobById(id));
    }


    @Operation(summary = "getJobsByCompanyId")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getJobs(
            @RequestParam String companyId
    ){
        return responseUtil.successResponse(jobService.getJobsByCompanyId(companyId));
    }

    @Operation(summary = "getJobs")
    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getAllJobs(){
        return responseUtil.successResponse(jobService.getAllJobs());
    }

    @Operation(summary = "getJobs")
    @AuthorizeValidator(UserRole.ADMIN)
    @RequestMapping(path = "/pages", method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getPagesJobs(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ){
        return responseUtil.successResponse(
                new PagingResponse(jobService.getPageAllJobs(pageNumber, pageSize))
        );
    }

    @Operation(summary = "getJobs")
    @RequestMapping(path = "/recommend", method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getAllJobs(
            @RequestParam(required = false) String major
    ){
        return responseUtil.successResponse(jobService.getListJobsRecommend(major));
    }

    @Operation(summary = "getJobsByCompanyId")
    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> searchJobs(
            @RequestParam(required = false) String searchKey
    ){
        return responseUtil.successResponse(jobService.searchJobs(searchKey));
    }

    @Operation(summary = "delete")
    @AuthorizeValidator({UserRole.ADMIN, UserRole.COMPANY_ADMIN, UserRole.COMPANY_ADMIN_MEMBER})
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<RestAPIResponse> deleteJob(
            @PathVariable(name = "id") String id
    ){
        return responseUtil.successResponse(jobService.deleteJob(id));
    }
}

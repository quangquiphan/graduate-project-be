package com.spring.boot.application.controller;

import com.spring.boot.application.common.AbstractBaseController;
import com.spring.boot.application.common.auth.AuthorizeValidator;
import com.spring.boot.application.common.enums.JobStatus;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.common.utils.RestAPIResponse;
import com.spring.boot.application.controller.model.request.company.ApplyJobRequest;
import com.spring.boot.application.controller.model.response.PagingResponse;
import com.spring.boot.application.services.job.JobService;
import com.spring.boot.application.services.job.UserJobService;
import com.spring.boot.application.services.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPath.USER_JOB_APIs)
public class UserJobController extends AbstractBaseController {
    final private UserJobService userJobService;
    final private UserService userService;

    public UserJobController(UserJobService userJobService, UserService userService) {
        this.userJobService = userJobService;
        this.userService = userService;
    }

    @AuthorizeValidator(UserRole.ADMIN)
    @RequestMapping(path = "/matches", method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getAllCandidateMatches(
            @RequestParam String major,
            @RequestParam String jobId
    ) {
        return responseUtil.successResponse(userService.matchesCandidate(major, jobId));
    }

    @AuthorizeValidator({UserRole.ADMIN, UserRole.COMPANY_ADMIN, UserRole.COMPANY_ADMIN_MEMBER, UserRole.COMPANY_MEMBER})
    @RequestMapping(path = "/applied", method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getAllCandidateApplied(
            @RequestParam String jobId
    ) {
        return responseUtil.successResponse(userJobService.getAllUserByJobIdAndJobStatus(jobId, JobStatus.APPLIED));
    }

    @AuthorizeValidator({UserRole.ADMIN, UserRole.COMPANY_ADMIN, UserRole.COMPANY_ADMIN_MEMBER, UserRole.COMPANY_MEMBER})
    @RequestMapping(path = "/rejected", method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getAllCandidateRejected(
            @RequestParam String jobId
    ) {
        return responseUtil.successResponse(userJobService.getAllUserByJobIdAndJobStatus(jobId, JobStatus.REJECTED));
    }

    @AuthorizeValidator({UserRole.ADMIN, UserRole.COMPANY_ADMIN, UserRole.COMPANY_ADMIN_MEMBER, UserRole.COMPANY_MEMBER})
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> changeStatus(
            @RequestBody ApplyJobRequest jobRequest
    ) {
        return responseUtil.successResponse(userJobService.changeStatus(jobRequest));
    }
}

package com.spring.boot.application.controller;

import com.spring.boot.application.common.AbstractBaseController;
import com.spring.boot.application.common.auth.AuthorizeValidator;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.common.utils.RestAPIResponse;
import com.spring.boot.application.services.dashboard.DashboardService;
import com.spring.boot.application.services.job.JobService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping(path = ApiPath.DASHBOARD_APIs)
@RestController
public class DashboardController extends AbstractBaseController {
    final private DashboardService dashboardService;
    final private JobService jobService;

    public DashboardController(
            DashboardService dashboardService,
            JobService jobService) {
        this.dashboardService = dashboardService;
        this.jobService = jobService;
    }

    @Operation(summary = "dashboard")
    @AuthorizeValidator(UserRole.ADMIN)
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> dashboard(
            HttpServletRequest request
    ) {
        jobService.deleteExpiryDateJob();
        return responseUtil.successResponse(dashboardService.getDashboard());
    }
}

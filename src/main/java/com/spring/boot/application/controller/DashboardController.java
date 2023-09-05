package com.spring.boot.application.controller;

import com.spring.boot.application.common.AbstractBaseController;
import com.spring.boot.application.common.utils.RestAPIResponse;
import com.spring.boot.application.controller.ApiPath;
import com.spring.boot.application.services.dashboard.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = ApiPath.DASHBOARD_APIs)
@RestController
public class DashboardController extends AbstractBaseController {
    final private DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Operation(summary = "dashboard")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> dashboard() {
        return responseUtil.successResponse(dashboardService.getDashboard());
    }
}

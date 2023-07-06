package com.spring.boot.application.controller;

import com.spring.boot.application.common.AbstractBaseController;
import com.spring.boot.application.common.auth.AuthorizeValidator;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.common.utils.RestAPIResponse;
import com.spring.boot.application.controller.model.request.experience.AddWorkHistory;
import com.spring.boot.application.services.experience.WorkHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPath.WORK_HISTORY_APIs)
public class WorkHistoryController extends AbstractBaseController {
    final private WorkHistoryService workHistoryService;

    public WorkHistoryController(
            WorkHistoryService workHistoryService
    ) {
        this.workHistoryService = workHistoryService;
    }

    @Operation(summary = "addWorkHistory")
    @AuthorizeValidator(UserRole.USER)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> addWorkHistory(
            @RequestBody AddWorkHistory workHistory
    ) {
        return responseUtil.successResponse(workHistoryService.addWorkHistory(workHistory));
    }

    @Operation(summary = "deleteWorkHistory")
    @AuthorizeValidator({UserRole.USER, UserRole.ADMIN})
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<RestAPIResponse> deleteWorkHistory(
            @PathVariable(name = "id") String id
    ) {
        return responseUtil.successResponse(workHistoryService.deleteWorkHistory(id));
    }
}

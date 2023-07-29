package com.spring.boot.application.controller;

import com.spring.boot.application.common.AbstractBaseController;
import com.spring.boot.application.common.auth.AuthorizeValidator;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.common.utils.RestAPIResponse;
import com.spring.boot.application.controller.model.request.experience.ProjectRequest;
import com.spring.boot.application.services.experience.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = ApiPath.PROJECT_APIs)
public class ProjectController extends AbstractBaseController {
    final private ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "getProject")
    @AuthorizeValidator({UserRole.ADMIN, UserRole.COMPANY_ADMIN, UserRole.COMPANY_ADMIN_MEMBER,
            UserRole.COMPANY_MEMBER, UserRole.USER})
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getProject(
            @PathVariable("id") String id
    ) {
        return responseUtil.successResponse(projectService.getProject(id));
    }

    @Operation(summary = "updateProject")
    @AuthorizeValidator({UserRole.ADMIN, UserRole.COMPANY_ADMIN, UserRole.COMPANY_ADMIN_MEMBER,
            UserRole.COMPANY_MEMBER, UserRole.USER})
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> getProject(
            @PathVariable("id") String id,
            @RequestBody ProjectRequest project
            ) {
        return responseUtil.successResponse(projectService.updateProject(id, project));
    }

    @Operation(summary = "deleteProject")
    @AuthorizeValidator({UserRole.ADMIN, UserRole.COMPANY_ADMIN, UserRole.COMPANY_ADMIN_MEMBER,
            UserRole.COMPANY_MEMBER, UserRole.USER})
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<RestAPIResponse> deleteProject(
            @PathVariable("id") String id
    ) {
        return responseUtil.successResponse(projectService.deleteProject(id));
    }
}

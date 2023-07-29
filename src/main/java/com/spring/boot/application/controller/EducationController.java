package com.spring.boot.application.controller;

import com.spring.boot.application.common.AbstractBaseController;
import com.spring.boot.application.common.auth.AuthorizeValidator;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.common.utils.RestAPIResponse;
import com.spring.boot.application.controller.model.request.education.EducationRequest;
import com.spring.boot.application.services.education.EducationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPath.EDUCATION_APIs)
public class EducationController extends AbstractBaseController {
    final private EducationService educationService;

    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @Operation(summary = "addEducation")
    @AuthorizeValidator(
            {UserRole.ADMIN, UserRole.COMPANY_ADMIN,
                    UserRole.COMPANY_ADMIN_MEMBER, UserRole.COMPANY_MEMBER, UserRole.USER})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> addEducation(
            @RequestBody EducationRequest education
            ){
        return responseUtil.successResponse(educationService.addEducation(education));
    }

    @Operation(summary = "getEducation")
    @AuthorizeValidator(
            {UserRole.ADMIN, UserRole.COMPANY_ADMIN,
                    UserRole.COMPANY_ADMIN_MEMBER, UserRole.COMPANY_MEMBER, UserRole.USER})
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getEducation(
            @PathVariable(name = "id") String id
    ){
        return responseUtil.successResponse(educationService.getEducation(id));
    }

    @Operation(summary = "updateEducation")
    @AuthorizeValidator(
            {UserRole.ADMIN, UserRole.COMPANY_ADMIN,
                    UserRole.COMPANY_ADMIN_MEMBER, UserRole.COMPANY_MEMBER, UserRole.USER})
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> updateEducation(
            @PathVariable("id") String id,
            @RequestBody EducationRequest education
    ){
        return responseUtil.successResponse(educationService.updateEducation(id, education));
    }

    @Operation(summary = "deleteEducation")
    @AuthorizeValidator(
            {UserRole.ADMIN, UserRole.COMPANY_ADMIN,
                    UserRole.COMPANY_ADMIN_MEMBER, UserRole.COMPANY_MEMBER, UserRole.USER})
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<RestAPIResponse> deleteEducation(
            @PathVariable("id") String id
    ){
        return responseUtil.successResponse(educationService.deleteEducation(id));
    }
}

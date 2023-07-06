package com.spring.boot.application.controller;

import com.spring.boot.application.common.AbstractBaseController;
import com.spring.boot.application.common.auth.AuthorizeValidator;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.common.utils.RestAPIResponse;
import com.spring.boot.application.controller.model.request.education.AddEducation;
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
            {UserRole.ADMIN, UserRole.ADMIN_COMPANY,
                    UserRole.ADMIN_COMPANY_MEMBER, UserRole.COMPANY_MEMBER, UserRole.USER})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> addEducation(
            @RequestBody AddEducation education
            ){
        return responseUtil.successResponse(educationService.addEducation(education));
    }

    @Operation(summary = "updateEducation")
    @AuthorizeValidator(
            {UserRole.ADMIN, UserRole.ADMIN_COMPANY,
                    UserRole.ADMIN_COMPANY_MEMBER, UserRole.COMPANY_MEMBER, UserRole.USER})
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> updateEducation(
            @PathVariable("id") String id,
            @RequestBody AddEducation education
    ){
        return responseUtil.successResponse(educationService.updateEducation(id, education));
    }

    @Operation(summary = "deleteEducation")
    @AuthorizeValidator(
            {UserRole.ADMIN, UserRole.ADMIN_COMPANY,
                    UserRole.ADMIN_COMPANY_MEMBER, UserRole.COMPANY_MEMBER, UserRole.USER})
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<RestAPIResponse> deleteEducation(
            @PathVariable("id") String id
    ){
        return responseUtil.successResponse(educationService.deleteEducation(id));
    }
}

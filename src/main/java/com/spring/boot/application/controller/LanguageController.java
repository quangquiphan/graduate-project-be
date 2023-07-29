package com.spring.boot.application.controller;

import com.spring.boot.application.common.AbstractBaseController;
import com.spring.boot.application.common.auth.AuthorizeValidator;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.common.utils.RestAPIResponse;
import com.spring.boot.application.controller.model.request.skill.LanguageRequest;
import com.spring.boot.application.services.skill.LanguageService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPath.LANGUAGE_APIs)
public class LanguageController extends AbstractBaseController {
    final private LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @Operation(summary = "addLanguage")
    @RequestMapping(method = RequestMethod.POST)
    @AuthorizeValidator({UserRole.ADMIN, UserRole.COMPANY_ADMIN})
    public ResponseEntity<RestAPIResponse> addLanguage(
            @RequestBody LanguageRequest addLanguage
            ) {
        return responseUtil.successResponse(languageService.addLanguage(addLanguage));
    }

    @Operation(summary = "updateLanguage")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    @AuthorizeValidator({UserRole.ADMIN, UserRole.COMPANY_ADMIN})
    public ResponseEntity<RestAPIResponse> updateLanguage(
            @PathVariable("id") String id,
            @RequestBody LanguageRequest addLanguage
    ) {
        return responseUtil.successResponse(languageService.updateLanguage(id, addLanguage));
    }

    @Operation(summary = "getAllLanguage")
    @RequestMapping(method = RequestMethod.GET)
    @AuthorizeValidator(
            {UserRole.ADMIN, UserRole.COMPANY_ADMIN, UserRole.COMPANY_MEMBER,
            UserRole.COMPANY_ADMIN_MEMBER, UserRole.USER}
    )
    public ResponseEntity<RestAPIResponse> getAllLanguage() {
        return responseUtil.successResponse(languageService.getAllLanguage());
    }

    @Operation(summary = "deleteLanguage")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @AuthorizeValidator({UserRole.ADMIN, UserRole.COMPANY_ADMIN})
    public ResponseEntity<RestAPIResponse> deleteLanguage(
            @PathVariable("id") String id
    ) {
        return responseUtil.successResponse(languageService.deleteLanguage(id));
    }
}

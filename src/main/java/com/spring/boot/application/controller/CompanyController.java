package com.spring.boot.application.controller;

import com.spring.boot.application.common.AbstractBaseController;
import com.spring.boot.application.common.auth.AuthorizeValidator;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.common.utils.RestAPIResponse;
import com.spring.boot.application.controller.model.request.company.AddCompany;
import com.spring.boot.application.services.company.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(ApiPath.COMPANY_APIs)
public class CompanyController extends AbstractBaseController {
    final private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Operation(summary = "addCompany")
    @AuthorizeValidator({UserRole.ADMIN, UserRole.ADMIN_COMPANY})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> addCompany(
            @RequestBody AddCompany addCompany
            ) {
        return responseUtil.successResponse(companyService.addCompany(addCompany));
    }

    @Operation(summary = "uploadAvatar")
    @AuthorizeValidator({UserRole.ADMIN, UserRole.ADMIN_COMPANY})
    @RequestMapping(path = "/upload-avatar/{id}", method = RequestMethod.PUT,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestAPIResponse> uploadAvatarCompany(
            @PathVariable(name = "id") String id,
            @RequestParam(name = "avatar") MultipartFile avatar
            ) throws IOException {
        return responseUtil.successResponse(companyService.uploadAvatar(id, avatar));
    }

    @Operation(summary = "uploadBackground")
    @AuthorizeValidator({UserRole.ADMIN, UserRole.ADMIN_COMPANY})
    @RequestMapping(path = "/upload-background/{id}", method = RequestMethod.PUT,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestAPIResponse> uploadBGCompany(
            @PathVariable(name = "id") String id,
            @RequestParam(name = "background") MultipartFile background
    ) throws IOException {
        return responseUtil.successResponse(companyService.uploadBackground(id, background));
    }

    @Operation(summary = "updateCompany")
    @AuthorizeValidator({UserRole.ADMIN, UserRole.ADMIN_COMPANY})
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> updateCompany(
            @PathVariable("id") String id,
            @RequestBody AddCompany updateCompany
    ) {
        return responseUtil.successResponse(companyService.updateCompany(id, updateCompany));
    }

    @Operation(summary = "getAllCompany")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getAllCompany(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return responseUtil.successResponse(companyService.getAllCompany(pageNumber, pageSize));
    }

    @Operation(summary = "getCompany")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getCompany(
            @PathVariable("id") String id
    ) {
        return responseUtil.successResponse(companyService.getCompany(id));
    }

    @Operation(summary = "deleteCompany")
    @AuthorizeValidator({UserRole.ADMIN})
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<RestAPIResponse> deleteCompany(
            @PathVariable("id") String id
    ) {
        return responseUtil.successResponse(companyService.deleteCompany(id));
    }
}

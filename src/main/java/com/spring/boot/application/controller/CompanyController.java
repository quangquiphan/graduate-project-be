package com.spring.boot.application.controller;

import com.spring.boot.application.common.AbstractBaseController;
import com.spring.boot.application.common.utils.RestAPIResponse;
import com.spring.boot.application.controller.model.request.company.AddCompany;
import com.spring.boot.application.services.company.CompanyService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(ApiPath.COMPANY_APIs)
public class CompanyController extends AbstractBaseController {
    final private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> addCompany(
            @RequestBody AddCompany addCompany
            ) {
        return responseUtil.successResponse(addCompany);
    }

    @RequestMapping(path = "/upload-avatar/{id}", method = RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestAPIResponse> uploadAvatarCompany(
            @PathVariable(name = "id") String id,
            @RequestParam(name = "avatar") MultipartFile avatar
            ) {
        return responseUtil.successResponse("");
    }

    @RequestMapping(path = "/upload-background/{id}", method = RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestAPIResponse> uploadBGCompany(
            @RequestBody AddCompany addCompany
    ) {
        return responseUtil.successResponse(addCompany);
    }
}

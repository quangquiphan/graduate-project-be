package com.spring.boot.application.controller;

import com.spring.boot.application.common.AbstractBaseController;
import com.spring.boot.application.common.auth.AuthorizeValidator;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.common.utils.RestAPIResponse;
import com.spring.boot.application.controller.model.response.PagingResponse;
import com.spring.boot.application.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@RestController
@RequestMapping(ApiPath.CANDIDATE_APIs)
public class CandidateController extends AbstractBaseController {
    final private UserService userService;

    @Value("${app.file.storage.mapping}")
    private String path;

    public CandidateController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "getCandidate")
    @AuthorizeValidator({UserRole.ADMIN, UserRole.COMPANY_ADMIN, UserRole.COMPANY_ADMIN_MEMBER, UserRole.COMPANY_MEMBER})
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getCandidate(
            @PathVariable(name = "id") String id
    ) throws IOException {
        return responseUtil.successResponse(userService.getCandidate(id));
    }

    @Operation(summary = "getCandidate")
    @AuthorizeValidator({UserRole.ADMIN, UserRole.COMPANY_ADMIN, UserRole.COMPANY_ADMIN_MEMBER, UserRole.COMPANY_MEMBER})
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getAllCandidate(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return responseUtil.successResponse(
                new PagingResponse(userService.getAllByRole(pageNumber, pageSize, UserRole.USER)));
    }

    @Operation(summary = "downloadCV")
    @RequestMapping(path = "/download/{id}", method = RequestMethod.GET)
    @AuthorizeValidator({UserRole.ADMIN, UserRole.COMPANY_ADMIN, UserRole.COMPANY_ADMIN_MEMBER,
            UserRole.COMPANY_MEMBER, UserRole.USER})
    public ResponseEntity<InputStreamResource> downloadCV(
            @PathVariable(name = "id") String id) throws IOException {
        HttpHeaders responseHeader = new HttpHeaders();

        Resource resource = new UrlResource(path + "cv/" + userService.downLoadCv(id));

        responseHeader.setContentType(MediaType.APPLICATION_PDF);
        responseHeader.setContentLength(resource.contentLength());
        responseHeader.set("Content-disposition", "attachment; filename=" + resource.getFilename());
        InputStream inputStream = resource.getInputStream();
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        return new ResponseEntity<InputStreamResource>(inputStreamResource, responseHeader, HttpStatus.OK);
    }
}

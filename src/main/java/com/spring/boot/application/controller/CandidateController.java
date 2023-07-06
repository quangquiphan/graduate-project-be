package com.spring.boot.application.controller;

import com.spring.boot.application.common.AbstractBaseController;
import com.spring.boot.application.common.auth.AuthorizeValidator;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.common.utils.RestAPIResponse;
import com.spring.boot.application.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(ApiPath.CANDIDATE_APIs)
public class CandidateController extends AbstractBaseController {
    final private UserService userService;

    public CandidateController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "getCandidate")
    @AuthorizeValidator({UserRole.ADMIN, UserRole.ADMIN_COMPANY, UserRole.ADMIN_COMPANY_MEMBER, UserRole.COMPANY_MEMBER})
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getCandidate(
            @PathVariable(name = "id") String id
    ) throws IOException {
        return responseUtil.successResponse(userService.getCandidate(id));
    }
}

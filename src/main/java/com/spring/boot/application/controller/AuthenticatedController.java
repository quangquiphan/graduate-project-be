package com.spring.boot.application.controller;

import com.spring.boot.application.common.AbstractBaseController;
import com.spring.boot.application.common.exceptions.ApplicationException;
import com.spring.boot.application.common.utils.Constant;
import com.spring.boot.application.common.utils.RestAPIResponse;
import com.spring.boot.application.common.utils.RestAPIStatus;
import com.spring.boot.application.controller.model.request.auth.ChangePassword;
import com.spring.boot.application.controller.model.request.auth.SignIn;
import com.spring.boot.application.services.session.SessionService;
import com.spring.boot.application.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(ApiPath.AUTHENTICATED_APIs)
public class AuthenticatedController extends AbstractBaseController {

    final private SessionService sessionService;
    final private PasswordEncoder passwordEncoder;
    final private UserService userService;

    public AuthenticatedController(
            SessionService sessionService,
            PasswordEncoder passwordEncoder,
            UserService userService) {
        this.sessionService = sessionService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Operation(summary = "signIn")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> signIn(
            @RequestBody SignIn signIn
    ) {
        return responseUtil.successResponse(sessionService.signIn(signIn, passwordEncoder));
    }

    @Operation(summary = "changePassword")
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> changePassword(
            @RequestBody ChangePassword changePassword,
            HttpServletRequest request
    ) {
        return responseUtil.successResponse(
                userService.changePassword(request.getHeader(Constant.HEADER_TOKEN), changePassword, passwordEncoder));
    }

    @Operation(summary = "getAuthInfo")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getAuthInfo(
            HttpServletRequest request
    ) {
        System.out.println(request.getHeader(Constant.HEADER_TOKEN));
        return responseUtil.successResponse(jwtTokenUtil.getUserIdFromJWT(request.getHeader(Constant.HEADER_TOKEN)));
    }

    @Operation(summary = "logout")
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<RestAPIResponse> logout(
            HttpServletRequest request
    ) {
        if (request.getHeader(Constant.HEADER_TOKEN).isEmpty() || request.getHeader(Constant.HEADER_TOKEN).equals("")) {
            throw new ApplicationException(RestAPIStatus.FORBIDDEN);
        }
        return responseUtil.successResponse(sessionService.signOut(request.getHeader(Constant.HEADER_TOKEN)));
    }
}

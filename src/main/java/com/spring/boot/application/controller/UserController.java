package com.spring.boot.application.controller;

import com.spring.boot.application.common.AbstractBaseController;
import com.spring.boot.application.common.auth.AuthorizeValidator;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.common.utils.RestAPIResponse;
import com.spring.boot.application.common.utils.RestAPIStatus;
import com.spring.boot.application.common.utils.Validator;
import com.spring.boot.application.controller.model.request.user.ApplyJob;
import com.spring.boot.application.controller.model.request.user.SignUp;
import com.spring.boot.application.controller.model.request.user.SubmitProfile;
import com.spring.boot.application.controller.model.response.user.UserResponse;
import com.spring.boot.application.services.notification.NotificationService;
import com.spring.boot.application.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(ApiPath.USER_APIs)
public class UserController extends AbstractBaseController {

    final private UserService userService;
    final private NotificationService notificationService;
    final PasswordEncoder passwordEncoder;

    public UserController(
            UserService userService,
            NotificationService notificationService,
            PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
        this.notificationService = notificationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "signUp")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> signUp(
            @RequestBody SignUp signUp
    ) {
        return responseUtil.successResponse(userService.signUp(signUp, passwordEncoder));
    }

    @Operation(summary = "verifyEmail")
    @RequestMapping(path = "/verify-email/{activeCode}", method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> verifyEmail(
            @PathVariable String activeCode
    ) {
        return responseUtil.successResponse(userService.verifyEmail(activeCode));
    }

    @Operation(summary = "uploadAvatar")
    @AuthorizeValidator(
            {UserRole.USER, UserRole.ADMIN, UserRole.COMPANY_ADMIN,
                    UserRole.COMPANY_MEMBER, UserRole.COMPANY_ADMIN_MEMBER})
    @RequestMapping(
            path = "/upload-avatar/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<RestAPIResponse> uploadAvatar(
            @PathVariable String id,
            @RequestPart("avatar") MultipartFile file
    ) throws IOException {
        return responseUtil.successResponse(userService.uploadAvatar(id, file));
    }

    @Operation(summary = "uploadCV")
    @AuthorizeValidator(
            {UserRole.USER, UserRole.ADMIN, UserRole.COMPANY_ADMIN,
                    UserRole.COMPANY_MEMBER, UserRole.COMPANY_ADMIN_MEMBER})
    @RequestMapping(
            path = "/upload-cv/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<RestAPIResponse> uploadCV(
            @PathVariable String id,
            @RequestPart("CV") MultipartFile file
    ) throws IOException {
        return responseUtil.successResponse(userService.uploadCV(id, file));
    }

    @Operation(summary = "updateProfile")
    @AuthorizeValidator(
            {UserRole.USER, UserRole.ADMIN, UserRole.COMPANY_ADMIN,
                    UserRole.COMPANY_MEMBER, UserRole.COMPANY_ADMIN_MEMBER})
    @RequestMapping(
            path = "/{id}",
            method = RequestMethod.PUT
    )
    public ResponseEntity<RestAPIResponse> updateProfile(
            @PathVariable String id,
            @RequestBody SubmitProfile submitProfile
    ) {
        return responseUtil.successResponse(userService.updateProfile(id, submitProfile));
    }

    @Operation(summary = "applyJob")
    @AuthorizeValidator(
            {UserRole.USER, UserRole.ADMIN})
    @RequestMapping(
            path = "/apply",
            method = RequestMethod.POST
    )
    public ResponseEntity<RestAPIResponse> applyJob(
            @RequestBody ApplyJob applyJob
    ) {
        UserResponse user = userService.apllyJob(applyJob);
        Validator.notNull(user, RestAPIStatus.BAD_PARAMS, "");
        notificationService.addNotification(applyJob.getUserId(), applyJob.getJobId(), "APPLIED_JOB");
        return responseUtil.successResponse(user);
    }

    @Operation(summary = "deleteUser")
    @AuthorizeValidator(UserRole.ADMIN)
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<RestAPIResponse> deleteUser(
            @PathVariable(name = "id") String id
    ) {
        return responseUtil.successResponse(userService.deleteUser(id));
    }
}
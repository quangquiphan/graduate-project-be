package com.spring.boot.application.controller;

import com.spring.boot.application.common.AbstractBaseController;
import com.spring.boot.application.common.auth.AuthUser;
import com.spring.boot.application.common.auth.AuthorizeValidator;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.common.utils.*;
import com.spring.boot.application.controller.model.request.user.AddMember;
import com.spring.boot.application.controller.model.request.user.ApplyJob;
import com.spring.boot.application.controller.model.request.user.SignUp;
import com.spring.boot.application.controller.model.request.user.SubmitProfile;
import com.spring.boot.application.controller.model.response.PagingResponse;
import com.spring.boot.application.controller.model.response.user.UserResponse;
import com.spring.boot.application.services.notification.NotificationService;
import com.spring.boot.application.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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

    @Operation(summary = "addMember")
    @AuthorizeValidator({UserRole.ADMIN, UserRole.COMPANY_ADMIN, UserRole.COMPANY_ADMIN_MEMBER})
    @RequestMapping(path = "/member", method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> addMember(
            @RequestBody AddMember addMember
            ) {
        return responseUtil.successResponse(userService.addMember(addMember));
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
    @RequestMapping(path = "/upload-avatar/{id}", method = RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestAPIResponse> uploadAvatar(
            @PathVariable String id,
            @RequestPart(required = true) MultipartFile file
    ) {
        try {
            return responseUtil.successResponse(userService.uploadAvatar(id, file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            @RequestPart(required = true) MultipartFile CV
    ) throws IOException {
        return responseUtil.successResponse(userService.uploadCV(id, CV));
    }

    @Operation(summary = "updateProfile")
    @AuthorizeValidator(
            {UserRole.USER, UserRole.ADMIN, UserRole.COMPANY_ADMIN,
                    UserRole.COMPANY_MEMBER, UserRole.COMPANY_ADMIN_MEMBER})
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> updateProfile(
            @PathVariable String id,
            @RequestBody SubmitProfile submitProfile
    ) {
        return responseUtil.successResponse(userService.updateProfile(id, submitProfile));
    }

    @Operation(summary = "updateMember")
    @AuthorizeValidator(
            {UserRole.COMPANY_ADMIN_MEMBER, UserRole.ADMIN, UserRole.COMPANY_ADMIN})
    @RequestMapping(
            path = "/member/{id}",
            method = RequestMethod.PUT
    )
    public ResponseEntity<RestAPIResponse> updateMember(
            @PathVariable String id,
            @RequestBody AddMember member
    ) {
        return responseUtil.successResponse(userService.updateMember(id, member));
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
        UserResponse user = userService.applyJob(applyJob);
        Validator.notNull(user, RestAPIStatus.BAD_PARAMS, "");
        notificationService.addNotification(applyJob.getUserId(), applyJob.getJobId(), "APPLIED_JOB");
        return responseUtil.successResponse(user);
    }

    @Operation(summary = "deleteUser")
    @AuthorizeValidator({UserRole.ADMIN, UserRole.COMPANY_ADMIN, UserRole.COMPANY_ADMIN_MEMBER})
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<RestAPIResponse> deleteUser(
            @PathVariable(name = "id") String id,
            HttpServletRequest request
    ) throws IOException{
        AuthUser authUser = jwtTokenUtil.getUserIdFromJWT(request.getHeader(Constant.HEADER_TOKEN));
        return responseUtil.successResponse(userService.deleteUser(id, authUser.getRole()));
    }

    @Operation(summary = "searchUser")
    @AuthorizeValidator({UserRole.ADMIN})
    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> searchUser(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return responseUtil.successResponse(
                new PagingResponse(userService.searchUser(keyword, pageNumber, pageSize))
        );
    }

    @Operation(summary = "getCompanyMember")
    @AuthorizeValidator({UserRole.ADMIN, UserRole.COMPANY_ADMIN, UserRole.COMPANY_ADMIN_MEMBER})
    @RequestMapping(path = "/member", method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getCompanyMember(
            @RequestParam String companyId,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return responseUtil.successResponse(
                new PagingResponse(userService.getAccountCompany(companyId, pageNumber, pageSize),
                        userService.getAccountCompany(companyId))
        );
    }

    @Operation(summary = "getUser")
    @AuthorizeValidator({UserRole.ADMIN, UserRole.COMPANY_ADMIN, UserRole.COMPANY_ADMIN_MEMBER, UserRole.USER,
            UserRole.COMPANY_MEMBER})
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getUser(
            @PathVariable String id
    ) throws IOException {
        return responseUtil.successResponse(userService.getCandidate(id));
    }
}
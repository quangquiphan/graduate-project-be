package com.spring.boot.application.services.user;

import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.controller.model.request.auth.ChangePassword;
import com.spring.boot.application.controller.model.request.auth.ForgotPasswordRequest;
import com.spring.boot.application.controller.model.request.auth.ResetPasswordRequest;
import com.spring.boot.application.controller.model.request.user.ApplyJob;
import com.spring.boot.application.controller.model.request.user.SignUp;
import com.spring.boot.application.controller.model.request.user.SubmitProfile;
import com.spring.boot.application.controller.model.response.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    //    Auth
    UserResponse signUp(SignUp signUp, PasswordEncoder passwordEncoder);

    String verifyEmail(String activeCode);

    String forgotPassword(ForgotPasswordRequest forgotPassword);

    UserResponse resetPassword(String resetCode, ResetPasswordRequest resetPassword, PasswordEncoder passwordEncoder);

    UserResponse changePassword(String token, ChangePassword changePassword, PasswordEncoder passwordEncoder);

    //    User
    UserResponse uploadAvatar(String id, MultipartFile file) throws IOException;

    Page<UserResponse> getAllByRole(int pageNumber, int pageSize, UserRole role);

    UserResponse uploadCV(String id, MultipartFile file) throws IOException;

    String downLoadCv(String id);

    UserResponse getCandidate(String id) throws IOException;

    UserResponse updateProfile(String id, SubmitProfile profile);

    UserResponse apllyJob(ApplyJob applyJob);

    String deleteUser(String id);
}

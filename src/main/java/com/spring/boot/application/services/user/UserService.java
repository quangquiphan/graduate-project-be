package com.spring.boot.application.services.user;

import com.spring.boot.application.controller.model.request.auth.ChangePassword;
import com.spring.boot.application.controller.model.request.user.SignUp;
import com.spring.boot.application.controller.model.response.user.CandidateResponse;
import com.spring.boot.application.entity.Session;
import com.spring.boot.application.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    Session signUp(SignUp signUp, PasswordEncoder passwordEncoder);
    User uploadAvatar(String id, MultipartFile file) throws IOException;
    User uploadCV(String id, MultipartFile file) throws IOException;
    User changePassword(String token, ChangePassword changePassword, PasswordEncoder passwordEncoder);
    CandidateResponse getCandidate(String id) throws IOException;
    String deleteUser(String id);
}

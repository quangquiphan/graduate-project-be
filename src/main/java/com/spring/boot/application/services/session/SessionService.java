package com.spring.boot.application.services.session;

import com.spring.boot.application.controller.model.request.auth.SignIn;
import com.spring.boot.application.entity.Session;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface SessionService {
    Session signIn(SignIn signIn, PasswordEncoder passwordEncoder);
    String signOut(String token);
}

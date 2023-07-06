package com.spring.boot.application.services.mail;

import com.spring.boot.application.entity.User;

public interface EmailService {
    void confirmRegisterAccount(User user);
}

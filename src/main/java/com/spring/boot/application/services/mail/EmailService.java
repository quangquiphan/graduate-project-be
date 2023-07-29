package com.spring.boot.application.services.mail;

import com.spring.boot.application.common.enums.AccountType;
import com.spring.boot.application.entity.Company;
import com.spring.boot.application.entity.User;

public interface EmailService {
    void confirmRegisterAccount(User user);
    void resetPassword(String activeCode, AccountType type);
}

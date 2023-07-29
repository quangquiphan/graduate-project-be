package com.spring.boot.application.controller.model.request.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.spring.boot.application.common.enums.UserRole;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignUp {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String major;
    private String email;
    private String passwordHash;
    private String confirmPassword;
    private UserRole role;
}

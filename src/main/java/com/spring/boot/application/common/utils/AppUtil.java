package com.spring.boot.application.common.utils;


import com.spring.boot.application.entity.Company;
import com.spring.boot.application.entity.User;
import com.spring.boot.application.repositories.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

@Component
public class AppUtil {
    public static final Random RANDOM = new SecureRandom();

    public static String generateSalt() {
        byte[] salt = new byte[Constant.SALT_LENGTH];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String getFileExtension(String file) {
        if (file == null) return null;

        String[] fileName = file.split("\\.");
        return fileName[fileName.length - 1];
    }

    public static String getUrlUser(User user, boolean isCV) {
        Validator.notNullAndNotEmpty(user, RestAPIStatus.NOT_FOUND, "User not found");
        if (!isCV && Validator.isValidParam(user.getAvatar()))
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/static/images/")
                    .path(user.getAvatar())
                    .toUriString();

        if (isCV && Validator.isValidParam(user.getCv()))
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/static/cv/")
                    .path(user.getCv())
                    .toUriString();

        return "";
    }

    public static String getUrlCompany(Company c, boolean isAvatar) throws IOException {
        Validator.notNullAndNotEmpty(c, RestAPIStatus.NOT_FOUND, "User not found");
        String path = "";
        if (!isAvatar && Validator.isValidParam(c.getAvatar()))
            path = c.getBackground();

        if (isAvatar && Validator.isValidParam(c.getBackground()))
            path = c.getAvatar();
        if (!Validator.isValidParam(path)) {
            return "";
        }

        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/static/images/")
                .path(path)
                .toUriString();
    }

    public static String getLogo() {

        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/static/images/")
                .path("TopWork-logo.png")
                .toUriString();
    }
}

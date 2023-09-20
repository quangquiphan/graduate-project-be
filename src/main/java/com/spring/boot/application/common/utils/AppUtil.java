package com.spring.boot.application.common.utils;


import com.spring.boot.application.common.exceptions.ApplicationException;
import com.spring.boot.application.entity.Company;
import com.spring.boot.application.entity.User;
import com.spring.boot.application.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

@Component
public class AppUtil {
    public static final Random RANDOM = new SecureRandom();

    @Value("${app.file.storage.mapping}")
    private static String path;

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

    public static User deleteURL(User user, boolean isCV) throws IOException {
        Validator.notNullAndNotEmpty(user, RestAPIStatus.NOT_FOUND, "User not found");
        try {
            if (!isCV && Validator.isValidParam(user.getAvatar())) {
                Path fileToDeletePath = Paths.get("src/main/resources/static/images/" + user.getAvatar());
                Files.delete(fileToDeletePath);
                user.setAvatar("");
                return user;
            }

            if (isCV && Validator.isValidParam(user.getCv())) {
                Path fileToDeletePath = Paths.get("src/main/resources/static/cv/" + user.getCv());
                Files.delete(fileToDeletePath);
                user.setCv("");
                return user;
            }
            return user;
        } catch (Exception e) {
            throw new ApplicationException(RestAPIStatus.FAIL);
        }
    }

    public static String getUrlCompany(Company c) throws IOException {
        Validator.notNullAndNotEmpty(c, RestAPIStatus.NOT_FOUND, "User not found");
        if (!Validator.isValidParam(c.getAvatar())) {
            return "";
        }

        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/static/images/")
                .path(c.getAvatar())
                .toUriString();
    }

    public static Company deleteAvatarCompany(Company c) throws IOException {
        Path fileToDeletePath = Paths.get("src/main/resources/static/images/" + c.getAvatar());
        Files.delete(fileToDeletePath);
        c.setAvatar("");

        return c;
    }

    public static String getLogo() {

        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/static/images/")
                .path("TopWork-logo.png")
                .toUriString();
    }
}

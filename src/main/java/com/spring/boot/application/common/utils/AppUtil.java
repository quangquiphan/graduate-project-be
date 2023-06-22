package com.spring.boot.application.common.utils;


import org.springframework.stereotype.Component;

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
}

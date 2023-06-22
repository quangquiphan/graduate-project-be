package com.spring.boot.application.services.user;

import com.spring.boot.application.common.enums.Status;
import com.spring.boot.application.common.exceptions.ApplicationException;
import com.spring.boot.application.common.utils.*;
import com.spring.boot.application.config.jwt.JwtTokenUtil;
import com.spring.boot.application.controller.model.request.auth.ChangePassword;
import com.spring.boot.application.controller.model.request.user.SignUp;
import com.spring.boot.application.entity.Session;
import com.spring.boot.application.entity.User;
import com.spring.boot.application.repositories.SessionRepository;
import com.spring.boot.application.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService{

    final private UserRepository userRepository;
    final private JwtTokenUtil jwtTokenUtil;
    final private SessionRepository sessionRepository;

    public UserServiceImpl(
            UserRepository userRepository,
            JwtTokenUtil jwtTokenUtil,
            SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.sessionRepository = sessionRepository;
    }

    @Value("${project.sources}")
    private String root;

    @Override
    public Session signUp(SignUp signUp, PasswordEncoder passwordEncoder) {
        User user = userRepository.getByEmailAndStatus(signUp.getEmail(), Status.ACTIVE);

        // Check valid params
        Validator.mustNull(user, RestAPIStatus.EXISTED, "User is exist");
        Validator.notNullAndNotEmptyParam(signUp.getEmail(), RestAPIStatus.BAD_PARAMS, "Email not null");
        Validator.validEmailAddressRegex(signUp.getEmail(), RestAPIStatus.BAD_PARAMS, "Email invalid");
        Validator.notNullAndNotEmptyParam(signUp.getPasswordHash(), RestAPIStatus.BAD_PARAMS, "Password not null");
        Validator.mustEquals(signUp.getPasswordHash(), signUp.getConfirmPassword(), RestAPIStatus.BAD_REQUEST, "Password and confirm password not match");

        User newUser = new User();

        newUser.setId(UniqueID.getUUID());
        newUser.setEmail(signUp.getEmail());
        newUser.setPasswordSalt(AppUtil.generateSalt());
        newUser.setPasswordHash(
                passwordEncoder.encode(signUp.getPasswordHash().concat(newUser.getPasswordSalt())));
        newUser.setRole(signUp.getRole());
        newUser.setStatus(Status.ACTIVE);

        userRepository.save(newUser);

        Session session = new Session();
        session.setAccessToken(jwtTokenUtil.generateAccessToken(newUser));
        session.setUserId(newUser.getId());
        session.setCreatedDate(DateUtil.convertToUTC(new Date()));
        session.setExpiryDate(DateUtil.addHoursToJavaUtilDate(new Date(), 24));
        return sessionRepository.save(session);
    }

    @Override
    public User uploadAvatar(String id, MultipartFile file) throws IOException {
        User user = userRepository.getById(id);
        Validator.notNullAndNotEmpty(user, RestAPIStatus.NOT_FOUND, "");

        return upload(user, "images/", file, false);
    }

    @Override
    public User uploadCV(String id, MultipartFile file) throws IOException {
        User user = userRepository.getById(id);
        Validator.notNullAndNotEmpty(user, RestAPIStatus.NOT_FOUND, "");

        return upload(user, "cv/", file, true);
    }

    @Override
    public User changePassword(String token, ChangePassword changePassword, PasswordEncoder passwordEncoder) {
        User user = userRepository.getByAccessToken(token);
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, "");
        Validator.mustEquals(changePassword.getNewPassword(), changePassword.getConfirmNewPassword(),
                RestAPIStatus.BAD_PARAMS, "");

        boolean checkOldPassword = passwordEncoder.matches(
                changePassword.getOldPassword().trim().concat(user.getPasswordSalt()).trim(),
                user.getPasswordHash());

        if (!checkOldPassword) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Wrong old password");
        }

        user.setPasswordHash(passwordEncoder.encode(changePassword.getNewPassword().concat(user.getPasswordSalt())));
        return userRepository.save(user);
    }

    @Override
    public String deleteUser(String id) {
        User user = userRepository.getById(id);
        Validator.notNullAndNotEmpty(user, RestAPIStatus.NOT_FOUND, "User not found");

        userRepository.delete(user);
        return "Delete successfully!";
    }

    private String getFileExtension(String file) {
        if (file == null) return null;

        String[] fileName = file.split("\\.");
        return fileName[fileName.length - 1];
    }

    private User upload(User user, String path, MultipartFile file, boolean isCv) throws IOException{
        // File name
        String name = user.getId() + "." + getFileExtension(file.getOriginalFilename());

        // Full path
        String filePath = root + path + name;

        // create folder if not create
        File folder = new File(root);

        if (!folder.exists()) {
            folder.mkdir();
        }

        // file copy
        Files.copy(file.getInputStream(), Paths.get(filePath));

        if (isCv) {
            user.setCv(name);
            return userRepository.save(user);
        }

        user.setAvatar(name);
        return userRepository.save(user);
    }
}

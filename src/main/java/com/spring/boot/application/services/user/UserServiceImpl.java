package com.spring.boot.application.services.user;

import com.spring.boot.application.common.enums.Status;
import com.spring.boot.application.common.exceptions.ApplicationException;
import com.spring.boot.application.common.utils.*;
import com.spring.boot.application.config.jwt.JwtTokenUtil;
import com.spring.boot.application.controller.model.request.auth.ChangePassword;
import com.spring.boot.application.controller.model.request.user.SignUp;
import com.spring.boot.application.controller.model.response.experience.WorkHistoryResponse;
import com.spring.boot.application.controller.model.response.user.CandidateResponse;
import com.spring.boot.application.entity.*;
import com.spring.boot.application.repositories.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    final private UserRepository userRepository;
    final private SessionRepository sessionRepository;
    final private EducationRepository educationRepository;
    final private WorkHistoryRepository workHistoryRepository;
    final private ProjectRepository projectRepository;
    final private JwtTokenUtil jwtTokenUtil;

    public UserServiceImpl(
            UserRepository userRepository,
            JwtTokenUtil jwtTokenUtil,
            SessionRepository sessionRepository,
            EducationRepository educationRepository,
            WorkHistoryRepository workHistoryRepository,
            ProjectRepository projectRepository
    ) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.sessionRepository = sessionRepository;
        this.educationRepository = educationRepository;
        this.workHistoryRepository = workHistoryRepository;
        this.projectRepository = projectRepository;
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
        newUser.setAvatar(null);
        newUser.setEmail(signUp.getEmail());
        newUser.setPasswordSalt(AppUtil.generateSalt());
        newUser.setPasswordHash(
                passwordEncoder.encode(signUp.getPasswordHash().concat(newUser.getPasswordSalt())));
        newUser.setRole(signUp.getRole());
        newUser.setStatus(Status.ACTIVE);
        newUser.setCv(null);

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

        return upload(user, "static/cv/", file, true);
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
    public CandidateResponse getCandidate(String id) throws IOException {
        User user = userRepository.getById(id);
        Validator.notNullAndNotEmpty(user, RestAPIStatus.NOT_FOUND, "");

        List<WorkHistoryResponse> workHistoryResponses = new ArrayList<>();
        List<Education> educations = educationRepository.getAllByUserId(user.getId());
        List<WorkHistory> workHistories = workHistoryRepository.getAllByUserId(user.getId());

        for (int i = 0; i < workHistories.size(); i++) {
            List<Project> projects = projectRepository.getAllByWorkHistoryId(workHistories.get(i).getId());
            workHistoryResponses.add(new WorkHistoryResponse(workHistories.get(i), projects));
        }
        return new CandidateResponse(user, getUrl(id, false), getUrl(id, true), workHistoryResponses, educations);
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

    private String getUrl(String id, boolean isCV) throws IOException{
        User user = userRepository.getById(id);
        Validator.notNullAndNotEmpty(user, RestAPIStatus.NOT_FOUND, "User not found");
        System.out.println(FileUtils.readFileToByteArray(new File("urc/resources/images" + user.getAvatar())));
        if (!isCV && Validator.isValidParam(user.getAvatar()))
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/images/")
                    .path(user.getAvatar())
                    .toUriString();

        if (isCV && Validator.isValidParam(user.getCv()))
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/cv/")
                    .path(user.getCv())
                    .toUriString();

        return "";
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

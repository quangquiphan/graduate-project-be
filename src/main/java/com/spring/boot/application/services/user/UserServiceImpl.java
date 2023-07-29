package com.spring.boot.application.services.user;

import com.spring.boot.application.common.enums.Status;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.common.exceptions.ApplicationException;
import com.spring.boot.application.common.utils.*;
import com.spring.boot.application.controller.model.request.auth.ChangePassword;
import com.spring.boot.application.controller.model.request.auth.ForgotPasswordRequest;
import com.spring.boot.application.controller.model.request.auth.ResetPasswordRequest;
import com.spring.boot.application.controller.model.request.user.SignUp;
import com.spring.boot.application.controller.model.response.experience.WorkHistoryResponse;
import com.spring.boot.application.controller.model.response.user.UserResponse;
import com.spring.boot.application.entity.*;
import com.spring.boot.application.repositories.*;
import com.spring.boot.application.services.mail.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private EmailService emailService;
    final private UserRepository userRepository;
    final private EducationRepository educationRepository;
    final private WorkHistoryRepository workHistoryRepository;
    final private ProjectRepository projectRepository;

    public UserServiceImpl(
            UserRepository userRepository,
            EducationRepository educationRepository,
            WorkHistoryRepository workHistoryRepository,
            ProjectRepository projectRepository
    ) {
        this.userRepository = userRepository;
        this.educationRepository = educationRepository;
        this.workHistoryRepository = workHistoryRepository;
        this.projectRepository = projectRepository;
    }

    @Value("${project.sources}")
    private String root;
    @Override
    public UserResponse signUp(SignUp signUp, PasswordEncoder passwordEncoder) {
        User exsitUser = userRepository.getByEmailAndStatus(signUp.getEmail(), Status.ACTIVE);

        // Check valid params
        Validator.mustNull(exsitUser, RestAPIStatus.EXISTED, "User is exist");
        Validator.notNullAndNotEmptyParam(signUp.getEmail(), RestAPIStatus.BAD_PARAMS, "Email not null");
        Validator.validEmailAddressRegex(signUp.getEmail(), RestAPIStatus.BAD_PARAMS, "Email invalid");
        Validator.notNullAndNotEmptyParam(signUp.getPasswordHash(), RestAPIStatus.BAD_PARAMS, "Password not null");
        Validator.mustEquals(signUp.getPasswordHash(), signUp.getConfirmPassword(), RestAPIStatus.BAD_REQUEST, "Password and confirm password not match");

        User user = new User();

        user.setId(UniqueID.getUUID());
        user.setFirstName(signUp.getFirstName());
        user.setLastName(signUp.getLastName());
        user.setEmail(signUp.getEmail());
        user.setMajor(signUp.getMajor());
        user.setPasswordSalt(AppUtil.generateSalt());
        user.setPasswordHash(
                passwordEncoder.encode(signUp.getPasswordHash().concat(user.getPasswordSalt())));
        user.setRole(signUp.getRole());
        user.setStatus(Status.IN_ACTIVE);
        user.setActiveCode(UniqueID.generateUniqueToken(16));

        userRepository.save(user);
        emailService.confirmRegisterAccount(user);
        return new UserResponse(user, AppUtil.getUrlUser(user, false), AppUtil.getUrlUser(user, true));
    }

    @Override
    public String verifyEmail(String activeCode) {
        User user = userRepository.getByActiveCodeAndStatus(activeCode, Status.IN_ACTIVE);
        Validator.notNullAndNotEmpty(user, RestAPIStatus.NOT_FOUND, "");

        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
        return "Email verified";
    }

    @Override
    public String forgotPassword(ForgotPasswordRequest forgotPassword) {
        Validator.notNullAndNotEmptyParam(forgotPassword.getEmail(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(forgotPassword.getType().toString(), RestAPIStatus.BAD_PARAMS, "");

        User user = userRepository.getByEmailAndStatus(forgotPassword.getEmail(), Status.ACTIVE);
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, "");

        user.setActiveCode(UniqueID.generateUniqueToken(16));

        userRepository.save(user);
        emailService.resetPassword(user.getActiveCode(), forgotPassword.getType());
        return "Successfully!";
    }

    @Override
    public UserResponse resetPassword(
            String resetCode,
            ResetPasswordRequest resetPassword,
            PasswordEncoder passwordEncoder
    ) {
        User user = userRepository.getByActiveCodeAndStatus(resetCode, Status.ACTIVE);
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, "");
        Validator.mustEquals(resetPassword.getPasswordHash(), resetPassword.getConfirmPassword(),
                RestAPIStatus.BAD_REQUEST, "Password and confirm password not match");

        user.setPasswordHash(
                passwordEncoder.encode(resetPassword.getPasswordHash().concat(user.getPasswordSalt())));

        return new UserResponse();
    }

    @Override
    public UserResponse changePassword(
            String token,
            ChangePassword changePassword,
            PasswordEncoder passwordEncoder
    ) {
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
        return new UserResponse(
                userRepository.save(user),
                AppUtil.getUrlUser(user, false),
                AppUtil.getUrlUser(user, true)
        );
    }

    @Override
    public UserResponse uploadAvatar(String id, MultipartFile file) throws IOException {
        User user = userRepository.getById(id);
        Validator.notNullAndNotEmpty(user, RestAPIStatus.NOT_FOUND, "");

        user = upload(user, "images/", file, false);
        return new UserResponse(user, AppUtil.getUrlUser(user, false), AppUtil.getUrlUser(user, true));
    }

    @Override
    public Page<UserResponse> getAllByRole(int pageNumber, int pageSize, UserRole role) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return userRepository.getAllByRole(role, pageRequest);
    }

    @Override
    public UserResponse uploadCV(String id, MultipartFile file) throws IOException {
        User user = userRepository.getById(id);
        Validator.notNullAndNotEmpty(user, RestAPIStatus.NOT_FOUND, "");

        user = upload(user, "cv/", file, false);
        return new UserResponse(user, AppUtil.getUrlUser(user, false), AppUtil.getUrlUser(user, true));
    }

    @Override
    public String downLoadCv(String id) {
        User user = userRepository.getById(id);
        return user.getCv();
    }

    @Override
    public UserResponse getCandidate(String id) throws IOException {
        User user = userRepository.getById(id);
        Validator.notNullAndNotEmpty(user, RestAPIStatus.NOT_FOUND, "");

        List<WorkHistoryResponse> workHistoryResponses = new ArrayList<>();
        List<Education> educations = educationRepository.getAllByUserId(user.getId());
        List<WorkHistory> workHistories = workHistoryRepository.getAllByUserId(user.getId());

        for (int i = 0; i < workHistories.size(); i++) {
            List<Project> projects = projectRepository.getAllByWorkHistoryId(workHistories.get(i).getId());
            workHistoryResponses.add(new WorkHistoryResponse(workHistories.get(i), projects));
        }
        return new UserResponse(user, AppUtil.getUrlUser(user, false),
                AppUtil.getUrlUser(user, true), workHistoryResponses, educations);
    }

    @Override
    public String deleteUser(String id) {
        User user = userRepository.getById(id);
        Validator.notNullAndNotEmpty(user, RestAPIStatus.NOT_FOUND, "User not found");

        userRepository.delete(user);
        return "Delete successfully!";
    }



    private User upload(User user, String path, MultipartFile file, boolean isCv) throws IOException{
        // File name
        String name = user.getId() + "." + AppUtil.getFileExtension(file.getOriginalFilename());

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

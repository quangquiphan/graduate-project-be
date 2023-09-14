package com.spring.boot.application.services.user;

import com.spring.boot.application.common.enums.AccountType;
import com.spring.boot.application.common.enums.JobStatus;
import com.spring.boot.application.common.enums.Status;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.common.exceptions.ApplicationException;
import com.spring.boot.application.common.utils.*;
import com.spring.boot.application.controller.model.request.auth.ChangePassword;
import com.spring.boot.application.controller.model.request.auth.ForgotPasswordRequest;
import com.spring.boot.application.controller.model.request.auth.ResetPasswordRequest;
import com.spring.boot.application.controller.model.request.user.AddMember;
import com.spring.boot.application.controller.model.request.user.ApplyJob;
import com.spring.boot.application.controller.model.request.user.SignUp;
import com.spring.boot.application.controller.model.request.user.SubmitProfile;
import com.spring.boot.application.controller.model.response.experience.WorkHistoryResponse;
import com.spring.boot.application.controller.model.response.job.UserJobResponse;
import com.spring.boot.application.controller.model.response.skill.UserLangResponse;
import com.spring.boot.application.controller.model.response.skill.UserSkillResponse;
import com.spring.boot.application.controller.model.response.user.UserResponse;
import com.spring.boot.application.entity.*;
import com.spring.boot.application.repositories.*;
import com.spring.boot.application.services.mail.EmailService;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    final private EmailService emailService;
    final private UserRepository userRepository;
    final private EducationRepository educationRepository;
    final private WorkHistoryRepository workHistoryRepository;
    final private ProjectRepository projectRepository;
    final private UserSkillRepository userSkillRepository;
    final private UserLangRepository userLangRepository;
    final private JobRepository jobRepository;
    final private UserJobRepository userJobRepository;
    final private SkillRepository skillRepository;
    final private LanguageRepository languageRepository;

    public UserServiceImpl(
            EmailService emailService, UserRepository userRepository,
            EducationRepository educationRepository,
            WorkHistoryRepository workHistoryRepository,
            ProjectRepository projectRepository,
            UserSkillRepository userSkillRepository,
            UserLangRepository userLangRepository,
            JobRepository jobRepository,
            UserJobRepository userJobRepository,
            SkillRepository skillRepository,
            LanguageRepository languageRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.educationRepository = educationRepository;
        this.workHistoryRepository = workHistoryRepository;
        this.projectRepository = projectRepository;
        this.userSkillRepository = userSkillRepository;
        this.userLangRepository = userLangRepository;
        this.jobRepository = jobRepository;
        this.userJobRepository = userJobRepository;
        this.skillRepository = skillRepository;
        this.languageRepository = languageRepository;
    }

    @Value("${project.sources}")
    private String root;

    SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.SHORT_DATE_FORMAT);

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
        user.setPhoneNumber(signUp.getPhoneNumber());

        userRepository.save(user);
        emailService.confirmRegisterAccount(user);
        return new UserResponse(user, AppUtil.getUrlUser(user, false), AppUtil.getUrlUser(user, true));
    }

    @Override
    public UserResponse addMember(AddMember member) {

        User exsitUser = userRepository.getByEmailAndStatus(member.getEmail(), Status.ACTIVE);

        // Check valid params
        Validator.mustNull(exsitUser, RestAPIStatus.EXISTED, "User is exist");
        Validator.notNullAndNotEmptyParam(member.getEmail(), RestAPIStatus.BAD_PARAMS, "Email not null");
        Validator.validEmailAddressRegex(member.getEmail(), RestAPIStatus.BAD_PARAMS, "Email invalid");

        User user = new User();

        user.setId(UniqueID.getUUID());
        user.setPasswordSalt(AppUtil.generateSalt());
        user.setFirstName(member.getFirstName());
        user.setLastName(member.getLastName());
        user.setEmail(member.getEmail());
        user.setRole(member.getRole());
        user.setStatus(Status.ACTIVE);
        user.setCompanyId(member.getCompanyId());
        user.setActiveCode(UniqueID.generateUniqueToken(16));

        userRepository.save(user);
        emailService.resetPassword(user.getActiveCode(), AccountType.COMPANY);
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
        userRepository.save(user);
        return new UserResponse(user, AppUtil.getUrlUser(user, false), AppUtil.getUrlUser(user, true));
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

        user = upload(user, "cv/", file, true);
        return new UserResponse(user, AppUtil.getUrlUser(user, false), AppUtil.getUrlUser(user, true));
    }

    @Override
    public String downLoadCv(String id) {
        User user = userRepository.getById(id);
        return user.getCv();
    }

    @Override
    public UserResponse getCandidate(String id) {
        User user = userRepository.getById(id);
        Validator.notNullAndNotEmpty(user, RestAPIStatus.NOT_FOUND, "");

        List<WorkHistoryResponse> workHistoryResponses = new ArrayList<>();
        List<Education> educations = educationRepository.getAllByUserId(user.getId());
        List<WorkHistory> workHistories = workHistoryRepository.getAllByUserId(user.getId());
        List<UserSkillResponse> skills = userSkillRepository.getAllByUserId(user.getId());
        List<UserLangResponse> languages = userLangRepository.getAllByUserId(user.getId());
        List<UserJob> userJobs = userJobRepository.getByUserId(user.getId());
        for (WorkHistory workHistory : workHistories) {
            List<Project> projects = projectRepository.getAllByWorkHistoryId(workHistory.getId());
            workHistoryResponses.add(new WorkHistoryResponse(workHistory, projects));
        }

        return new UserResponse(user, AppUtil.getUrlUser(user, false),
                AppUtil.getUrlUser(user, true), workHistoryResponses,
                educations, skills, languages, userJobs);
    }

    @Override
    public UserResponse updateMember(String id, AddMember member) {
        User user = userRepository.getById(id);
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, "");

        user.setLastName(member.getLastName());
        user.setFirstName(member.getFirstName());
        user.setEmail(member.getEmail());
        user.setRole(member.getRole());
        userRepository.save(user);
        return new UserResponse(user);
    }

    @Override
    public UserResponse updateProfile(String id, SubmitProfile profile) {
        User user = userRepository.getById(id);
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, "");

        user.setFirstName(profile.getFirstName());
        user.setLastName(profile.getLastName());
        user.setDateOfBirth(DateUtil.convertToShortDate(profile.getDateOfBirth()));
        user.setGender(profile.getGender());
        user.setPhoneNumber(profile.getPhoneNumber());
        user.setPosition(profile.getPosition());
        user.setSummary(profile.getSummary());
        user.setMajor(profile.getMajor());
        user.setLink(profile.getLink());
        user.setAddress(profile.getAddress());
        user.setYearExperience(profile.getYearExperience());


        if (Validator.isValidObject(profile.getSkills())) {
            List<UserSkill> skills = new ArrayList<>();

            for (int i = 0; i < profile.getSkills().size(); i++) {
                if (!Validator.isValidParam(profile.getSkills().get(i).getStatus())){
                    profile.getSkills().get(i).setStatus(Status.IN_ACTIVE);
                }

                if (Validator.isValidParam(profile.getSkills().get(i).getId()) &&
                        Validator.mustEquals(profile.getSkills().get(i).getStatus(), Status.IN_ACTIVE)) {

                    UserSkill skill = userSkillRepository.getBySkillIdAndStatus(profile.getSkills().get(i).getSkillId(),
                            Status.ACTIVE);
                    userSkillRepository.delete(skill);
                }

                if (!Validator.isValidObject(profile.getSkills().get(i).getId()) &&
                        Validator.mustEquals(profile.getSkills().get(i).getStatus(), Status.ACTIVE)) {
                    UserSkill s = new UserSkill();
                    s.setId(UniqueID.getUUID());
                    s.setUserId(user.getId());
                    s.setSkillId(profile.getSkills().get(i).getSkillId());
                    s.setStatus(profile.getSkills().get(i).getStatus());

                    skills.add(s);
                }
            }
            userSkillRepository.saveAll(skills);
        }

        if (Validator.isValidObject(profile.getLanguages())) {
            List<UserLang> langs = new ArrayList<>();

            for (int i = 0; i < profile.getLanguages().size(); i++) {
                if (!Validator.isValidParam(profile.getLanguages().get(i).getStatus())) {
                    profile.getLanguages().get(i).setStatus(Status.IN_ACTIVE);
                }

                if (Validator.isValidParam(profile.getLanguages().get(i).getId()) &&
                        Validator.mustEquals(profile.getLanguages().get(i).getStatus(), Status.IN_ACTIVE)) {

                    UserLang lang = userLangRepository.getByLangIdAndStatus(profile.getLanguages().get(i).getLangId(),
                            Status.ACTIVE);
                    userLangRepository.delete(lang);
                }

                if (!Validator.isValidObject(profile.getLanguages().get(i).getId()) &&
                        Validator.mustEquals(profile.getLanguages().get(i).getStatus(), Status.ACTIVE)) {
                    UserLang l = new UserLang();
                    l.setId(UniqueID.getUUID());
                    l.setUserId(user.getId());
                    l.setLangId(profile.getLanguages().get(i).getLangId());
                    l.setStatus(profile.getLanguages().get(i).getStatus());

                    langs.add(l);
                }
            }
            userLangRepository.saveAll(langs);
        }

        userRepository.save(user);
        return new UserResponse(user);
    }

    @Override
    public UserResponse applyJob(ApplyJob applyJob) {
        User user = userRepository.getById(applyJob.getUserId());
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, "");

        Job job = jobRepository.getById(applyJob.getJobId());
        Validator.notNull(job, RestAPIStatus.NOT_FOUND, "");

        UserJob userJob = new UserJob();
        userJob.setId(UniqueID.getUUID());
        userJob.setJobId(job.getId());
        userJob.setUserId(user.getId());
        userJob.setStatus(applyJob.getStatus());

        userJobRepository.save(userJob);
        return new UserResponse(user);
    }

    @Override
    public List<UserJobResponse> matchesCandidate(String major, String jobId) {
        List<UserJobResponse> userResponses = userRepository.getAllByMajor(major, jobId);
        List<UserJobResponse> responses = new ArrayList<>();
        for (UserJobResponse userJobResponse : userResponses) {
            User user = userRepository.getById(userJobResponse.getUserId());
            List<UserSkillResponse> skills = userSkillRepository.getAllByUserId(userJobResponse.getUserId());
            List<UserLangResponse> languages = userLangRepository.getAllByUserId(userJobResponse.getUserId());
            responses.add(new UserJobResponse(userJobResponse, AppUtil.getUrlUser(user, false),
                    AppUtil.getUrlUser(user, true), skills, languages));
        }

        return responses;
    }

    @Override
    public Page<UserResponse> searchUser(String keyword, int pageNumber, int pageSize) {
        PageRequest request = PageRequest.of(pageNumber - 1, pageSize);
        if (Validator.isValidParam(keyword)){
            return userRepository.searchCandidate(keyword, request);
        }

        return userRepository.getAllByRole(UserRole.USER, request);
    }

    @Override
    public Page<UserResponse> getAccountCompany(String companyId, int pageNumber, int pageSize) {
        PageRequest request = PageRequest.of(pageNumber - 1, pageSize);
        return userRepository.getAllByCompanyId(companyId, request);
    }

    @Override
    public List<UserResponse> getAccountCompany(String companyId) {
        List<UserResponse> responses = new ArrayList<>();
        List<User> accounts = userRepository.getAllByCompanyId(companyId);

        for (int i = 0; i < accounts.size(); i++) {
            User user = userRepository.getById(accounts.get(i).getId());
            responses.add(new UserResponse(user, AppUtil.getUrlUser(user, false),
                    AppUtil.getUrlUser(user, true)));
        }
        return responses;
    }

    @Override
    public String deleteUser(String id, UserRole role) {
        User user = userRepository.getById(id);
        Validator.notNullAndNotEmpty(user, RestAPIStatus.NOT_FOUND, "User not found");
        List<UserSkill> userSkills = userSkillRepository.getAllByUser(user.getId());
        List<UserLang> userLangs = userLangRepository.getAllByUser(user.getId());
        List<UserJob> userJobs = userJobRepository.getByUserId(user.getId());
        List<Education> educations = educationRepository.getAllByUserId(user.getId());
        List<WorkHistory> workHistories = workHistoryRepository.getAllByUserId(user.getId());

        if (!checkRole(user, role)) {
            throw new ApplicationException(RestAPIStatus.FORBIDDEN, "Delete failed!");
        }

        userRepository.delete(user);
        userSkillRepository.deleteAll(userSkills);
        userLangRepository.deleteAll(userLangs);
        userJobRepository.deleteAll(userJobs);
        educationRepository.deleteAll(educations);
        for (WorkHistory workHistory : workHistories) {
            List<Project> projects = projectRepository.getAllByWorkHistoryId(workHistory.getId());
            projectRepository.deleteAll(projects);
        }
        workHistoryRepository.deleteAll(workHistories);
        return "Delete successfully!";
    }


    private User upload(User user, String path, MultipartFile file, boolean isCv) throws IOException {
        // File name
        String name = user.getId() + "-" + new Date().getTime() + "." + AppUtil.getFileExtension(file.getOriginalFilename());

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

    private boolean checkRole(User u, UserRole role) {
        switch (role) {
            case ADMIN: {
                break;
            }

            case COMPANY_ADMIN: {
                if (u.getRole().equals(UserRole.ADMIN) ||
                        u.getRole().equals(UserRole.COMPANY_ADMIN)) {
                    return false;
                }
                break;
            }

            case COMPANY_ADMIN_MEMBER: {
                if (u.getRole().equals(UserRole.ADMIN) ||
                        u.getRole().equals(UserRole.COMPANY_ADMIN) ||
                        u.getRole().equals(UserRole.COMPANY_ADMIN_MEMBER)) {
                    return false;
                }
                break;
            }

            case COMPANY_MEMBER:
            case USER: {
                return false;
            }
        }

        return true;
    }
}

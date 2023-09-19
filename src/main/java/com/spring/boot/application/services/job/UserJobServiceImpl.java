package com.spring.boot.application.services.job;

import com.spring.boot.application.common.enums.JobStatus;
import com.spring.boot.application.common.exceptions.ApplicationException;
import com.spring.boot.application.common.utils.AppUtil;
import com.spring.boot.application.common.utils.RestAPIStatus;
import com.spring.boot.application.common.utils.Validator;
import com.spring.boot.application.controller.model.request.company.ApplyJobRequest;
import com.spring.boot.application.controller.model.response.job.JobsAppliedResponse;
import com.spring.boot.application.controller.model.response.job.LangJobResponse;
import com.spring.boot.application.controller.model.response.job.SkillJobResponse;
import com.spring.boot.application.controller.model.response.job.UserJobResponse;
import com.spring.boot.application.controller.model.response.skill.UserLangResponse;
import com.spring.boot.application.controller.model.response.skill.UserSkillResponse;
import com.spring.boot.application.entity.Company;
import com.spring.boot.application.entity.User;
import com.spring.boot.application.entity.UserJob;
import com.spring.boot.application.repositories.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserJobServiceImpl implements UserJobService{
    final private UserJobRepository userJobRepository;
    final private UserSkillRepository userSkillRepository;
    final private UserLangRepository userLangRepository;
    final private UserRepository userRepository;
    final private SkillJobRepository skillJobRepository;
    final private LanguageJobRepository languageJobRepository;
    final private CompanyRepository companyRepository;

    public UserJobServiceImpl(UserJobRepository userJobRepository,
                              UserSkillRepository userSkillRepository,
                              UserLangRepository userLangRepository,
                              UserRepository userRepository,
                              SkillJobRepository skillJobRepository,
                              LanguageJobRepository languageJobRepository,
                              CompanyRepository companyRepository) {
        this.userJobRepository = userJobRepository;
        this.userSkillRepository = userSkillRepository;
        this.userLangRepository = userLangRepository;
        this.userRepository = userRepository;
        this.skillJobRepository = skillJobRepository;
        this.languageJobRepository = languageJobRepository;
        this.companyRepository = companyRepository;
    }


    @Override
    public String changeStatus(ApplyJobRequest jobRequest) {
        UserJob userJob = userJobRepository.getById(jobRequest.getUserJobId());
        Validator.notNull(userJob, RestAPIStatus.NOT_FOUND, "");

        userJob.setStatus(jobRequest.getStatus());
        userJobRepository.save(userJob);
        return "Successfully";
    }

    @Override
    public List<UserJobResponse> getAllUserByJobIdAndJobStatus(String jobId, JobStatus jobStatus) {
        List<UserJobResponse> list = userJobRepository.getAllByJobIdAndStatus(jobId, jobStatus);
        List<UserJobResponse> responses = new ArrayList<>();
        for (UserJobResponse userJobResponse : list) {
            User user = userRepository.getById(userJobResponse.getUserId());
            List<UserSkillResponse> skills = userSkillRepository.getAllByUserId(userJobResponse.getUserId());
            List<UserLangResponse> languages = userLangRepository.getAllByUserId(userJobResponse.getUserId());
            responses.add(new UserJobResponse(userJobResponse, AppUtil.getUrlUser(user, false), AppUtil.getUrlUser(user, true), skills, languages));
        }
        return responses;
    }

    @Override
    public List<JobsAppliedResponse> getJobsApplied(String userId) {
        List<JobsAppliedResponse> responses = new ArrayList<>();
        List<JobsAppliedResponse> list = userJobRepository.getAllJobApplied(userId);

        for (JobsAppliedResponse item : list) {
            List<SkillJobResponse> skills = skillJobRepository.getAllByJobId(item.getJobId());
            List<LangJobResponse> langs = languageJobRepository.getAllByJobId(item.getJobId());

            responses.add(new JobsAppliedResponse(item, companyAvatar(item.getCompanyId()), langs, skills));
        }

        return responses;
    }

    @Override
    public String deleteUserJob(String id) {
        UserJob uj = userJobRepository.getById(id);
        Validator.notNullAndNotEmpty(uj, RestAPIStatus.NOT_FOUND, "");
        userJobRepository.delete(uj);
        return "Delete successfully!";
    }


    private String companyAvatar(String companyId) {
        Company c = companyRepository.getById(companyId);
        if (!Validator.isValidObject(c)) {
            return "";
        }
        try {
            return AppUtil.getUrlCompany(c);
        } catch (Exception e) {
            throw new ApplicationException(RestAPIStatus.NO_RESULT, e.getMessage());
        }
    }
}

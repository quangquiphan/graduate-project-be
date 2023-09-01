package com.spring.boot.application.services.job;

import com.spring.boot.application.common.enums.JobStatus;
import com.spring.boot.application.common.utils.AppUtil;
import com.spring.boot.application.common.utils.RestAPIStatus;
import com.spring.boot.application.common.utils.Validator;
import com.spring.boot.application.controller.model.request.company.ApplyJobRequest;
import com.spring.boot.application.controller.model.response.job.UserJobResponse;
import com.spring.boot.application.controller.model.response.skill.UserLangResponse;
import com.spring.boot.application.controller.model.response.skill.UserSkillResponse;
import com.spring.boot.application.entity.User;
import com.spring.boot.application.entity.UserJob;
import com.spring.boot.application.repositories.UserJobRepository;
import com.spring.boot.application.repositories.UserLangRepository;
import com.spring.boot.application.repositories.UserRepository;
import com.spring.boot.application.repositories.UserSkillRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserJobServiceImpl implements UserJobService{
    final private UserJobRepository userJobRepository;
    final private UserSkillRepository userSkillRepository;
    final private UserLangRepository userLangRepository;
    final private UserRepository userRepository;

    public UserJobServiceImpl(UserJobRepository userJobRepository,
                              UserSkillRepository userSkillRepository,
                              UserLangRepository userLangRepository,
                              UserRepository userRepository) {
        this.userJobRepository = userJobRepository;
        this.userSkillRepository = userSkillRepository;
        this.userLangRepository = userLangRepository;
        this.userRepository = userRepository;
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
}

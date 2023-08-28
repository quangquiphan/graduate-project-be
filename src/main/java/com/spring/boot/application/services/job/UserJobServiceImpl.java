package com.spring.boot.application.services.job;

import com.spring.boot.application.common.enums.JobStatus;
import com.spring.boot.application.common.utils.RestAPIStatus;
import com.spring.boot.application.common.utils.Validator;
import com.spring.boot.application.entity.UserJob;
import com.spring.boot.application.repositories.UserJobRepository;
import org.springframework.stereotype.Service;

@Service
public class UserJobServiceImpl implements UserJobService{
    final private UserJobRepository userJobRepository;

    public UserJobServiceImpl(UserJobRepository userJobRepository) {
        this.userJobRepository = userJobRepository;
    }


    @Override
    public String changeStatus(String userJobId, JobStatus status) {
        UserJob userJob = userJobRepository.getById(userJobId);
        Validator.notNull(userJob, RestAPIStatus.NOT_FOUND, "");

        userJob.setStatus(status);
        userJobRepository.save(userJob);
        return "Successfully";
    }
}

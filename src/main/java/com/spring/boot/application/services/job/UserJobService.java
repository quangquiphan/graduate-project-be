package com.spring.boot.application.services.job;

import com.spring.boot.application.common.enums.JobStatus;
import com.spring.boot.application.controller.model.response.job.UserJobResponse;

import java.util.List;

public interface UserJobService {
    String changeStatus(String userJobId, JobStatus status);
    List<UserJobResponse> getAllUserByJobIdAndJobStatus(String jobId, JobStatus jobStatus);
}

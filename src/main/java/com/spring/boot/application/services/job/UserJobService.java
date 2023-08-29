package com.spring.boot.application.services.job;

import com.spring.boot.application.common.enums.JobStatus;

import java.util.List;

public interface UserJobService {
    String changeStatus(String userJobId, JobStatus status);
    List<?> getAllUserByJobIdAndJobStatus(String jobId, JobStatus jobStatus);
}

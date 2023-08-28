package com.spring.boot.application.services.job;

import com.spring.boot.application.common.enums.JobStatus;

public interface UserJobService {
    String changeStatus(String userJobId, JobStatus status);
}

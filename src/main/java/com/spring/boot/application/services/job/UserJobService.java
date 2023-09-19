package com.spring.boot.application.services.job;

import com.spring.boot.application.common.enums.JobStatus;
import com.spring.boot.application.controller.model.request.company.ApplyJobRequest;
import com.spring.boot.application.controller.model.response.job.JobsAppliedResponse;
import com.spring.boot.application.controller.model.response.job.UserJobResponse;

import java.util.List;

public interface UserJobService {
    String changeStatus(ApplyJobRequest jobRequest);
    List<UserJobResponse> getAllUserByJobIdAndJobStatus(String jobId, JobStatus jobStatus);
    List<JobsAppliedResponse> getJobsApplied(String userId);
    String deleteUserJob(String id);
}

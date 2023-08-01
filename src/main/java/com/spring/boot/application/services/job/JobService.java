package com.spring.boot.application.services.job;

import com.spring.boot.application.controller.model.request.company.JobRequest;
import com.spring.boot.application.controller.model.response.job.JobResponse;
import com.spring.boot.application.entity.Job;
import org.springframework.data.domain.Page;

import java.util.List;

public interface JobService {
    Job addJob(JobRequest jobRequest);
    Job updateJob(String id, JobRequest jobRequest);
    List<JobResponse> getJobsByCompanyId(String companyId);
    Page<Job> getPageJobByCompanyId(String companyId, int pageNumber, int pageSize);
    List<JobResponse> getAllJobs();
    Page<Job> getPageAllJobs(int pageNumber, int pageSize);
    List<JobResponse> searchJobs(String searchKey);
}

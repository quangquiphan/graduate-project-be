package com.spring.boot.application.services.job;

import com.spring.boot.application.controller.model.request.company.JobRequest;
import com.spring.boot.application.controller.model.response.job.JobResponse;
import com.spring.boot.application.entity.Job;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface JobService {
    Job addJob(JobRequest jobRequest);
    Job updateJob(String id, JobRequest jobRequest);
    JobResponse getJobById(String id) throws IOException;
    List<JobResponse> getJobsByCompanyId(String companyId);
    List<JobResponse> getAllJobs();
    Page<JobResponse> getPageAllJobs(int pageNumber, int pageSize);
    List<JobResponse> getListJobsRecommend(String major);
    List<JobResponse> searchJobs(String searchKey);
    String deleteJob(String id);
    String deleteExpiryDateJob();
}

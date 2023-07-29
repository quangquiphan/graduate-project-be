package com.spring.boot.application.services.job;

import com.spring.boot.application.common.enums.Status;
import com.spring.boot.application.common.utils.AppUtil;
import com.spring.boot.application.common.utils.RestAPIStatus;
import com.spring.boot.application.common.utils.UniqueID;
import com.spring.boot.application.common.utils.Validator;
import com.spring.boot.application.controller.model.request.company.JobRequest;
import com.spring.boot.application.controller.model.response.job.JobResponse;
import com.spring.boot.application.controller.model.response.job.LangJobResponse;
import com.spring.boot.application.controller.model.response.job.SkillJobResponse;
import com.spring.boot.application.controller.model.response.skill.SkillResponse;
import com.spring.boot.application.entity.Company;
import com.spring.boot.application.entity.Job;
import com.spring.boot.application.entity.LanguageJob;
import com.spring.boot.application.entity.SkillJob;
import com.spring.boot.application.repositories.CompanyRepository;
import com.spring.boot.application.repositories.JobRepository;
import com.spring.boot.application.repositories.LanguageJobRepository;
import com.spring.boot.application.repositories.SkillJobRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {
    final private CompanyRepository companyRepository;
    final private JobRepository jobRepository;
    final private SkillJobRepository skillJobRepository;
    final private LanguageJobRepository languageJobRepository;

    public JobServiceImpl(CompanyRepository companyRepository, JobRepository jobRepository,
                          SkillJobRepository skillJobRepository,
                          LanguageJobRepository languageJobRepository
    ) {
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.skillJobRepository = skillJobRepository;
        this.languageJobRepository = languageJobRepository;
    }

    @Override
    public Job addJob(JobRequest jobRequest) {
        Validator.notNullAndNotEmptyParam(jobRequest.getJobName(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(jobRequest.getJobPosition(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(jobRequest.getCategoryJob(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(jobRequest.getRequired(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(jobRequest.getBenefited(), RestAPIStatus.BAD_PARAMS, "");

        Job job = new Job();

        job.setId(UniqueID.getUUID());
        job.setJobName(jobRequest.getJobName());
        job.setJobPosition(jobRequest.getJobPosition());
        job.setDescription(jobRequest.getDescription());
        job.setRequired(jobRequest.getRequired());
        job.setBenefited(jobRequest.getBenefited());
        job.setCity(jobRequest.getCity());
        job.setSalary(jobRequest.getSalary());
        job.setExpiryDate(jobRequest.getExpiryDate());
        job.setCategoryJob(jobRequest.getCategoryJob());
        job.setCompanyId(jobRequest.getCompanyId());

        List<SkillJob> skillJobs = new ArrayList<>();
        List<LanguageJob> languageJobs = new ArrayList<>();

        for (int i = 0; i < jobRequest.getLanguages().size(); i++) {
            LanguageJob langJob = new LanguageJob();

            langJob.setId(UniqueID.getUUID());
            langJob.setLanguageId(jobRequest.getLanguages().get(i).getLanguageId());
            langJob.setJobId(job.getId());
            langJob.setStatus(Status.ACTIVE);
            languageJobs.add(langJob);

        }

        for (int i = 0; i < jobRequest.getSkills().size(); i++) {
            SkillJob skillJob = new SkillJob();

            skillJob.setId(UniqueID.getUUID());
            skillJob.setSkillId(jobRequest.getSkills().get(i).getSkillId());
            skillJob.setJobId(job.getId());
            skillJob.setStatus(Status.ACTIVE);
            skillJobs.add(skillJob);
        }

        jobRepository.save(job);
        skillJobRepository.saveAll(skillJobs);
        languageJobRepository.saveAll(languageJobs);
        return job;
    }

    @Override
    public Job updateJob(String id, JobRequest jobRequest) {
        return null;
    }

    @Override
    public List<JobResponse> getJobs(String companyId) {
        Company company = companyRepository.getById(companyId);
        Validator.notNull(company, RestAPIStatus.NOT_FOUND, "");
        List<Job> jobs = jobRepository.getAllJobCompany(companyId);

        try {
            String url = AppUtil.getUrlCompany(company, true);

            List<JobResponse> jobResponses = new ArrayList<>();
            for (int i = 0; i < jobs.size(); i++) {
                List<SkillJobResponse> skills = skillJobRepository.getAllByJobId(jobs.get(i).getId());
                List<LangJobResponse> langs = languageJobRepository.getAllByJobId(jobs.get(i).getId());
                jobResponses.add(new JobResponse(jobs.get(i), url, company.getCompanyName(), skills, langs));
            }
            return jobResponses;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<Job> getPageJob(String companyId, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return jobRepository.getAllJobCompany(companyId, pageRequest);
    }
}

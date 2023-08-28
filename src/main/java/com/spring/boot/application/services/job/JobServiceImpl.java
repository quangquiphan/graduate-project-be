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
import com.spring.boot.application.entity.*;
import com.spring.boot.application.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobServiceImpl implements JobService {
    final private CompanyRepository companyRepository;
    final private JobRepository jobRepository;
    final private SkillJobRepository skillJobRepository;
    final private LanguageJobRepository languageJobRepository;
    final private UserJobRepository userJobRepository;

    public JobServiceImpl(CompanyRepository companyRepository, JobRepository jobRepository,
                          SkillJobRepository skillJobRepository,
                          LanguageJobRepository languageJobRepository,
                          UserJobRepository userJobRepository) {
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.skillJobRepository = skillJobRepository;
        this.languageJobRepository = languageJobRepository;
        this.userJobRepository = userJobRepository;
    }

    @Override
    public Job addJob(JobRequest jobRequest) {
        Validator.notNullAndNotEmptyParam(jobRequest.getJobName(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(jobRequest.getJobPosition(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(jobRequest.getCategoryJob(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(jobRequest.getRequired(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(jobRequest.getBenefited(), RestAPIStatus.BAD_PARAMS, "");

        Job job = newJob(jobRequest);

        List<SkillJob> skillJobs = new ArrayList<>();
        List<LanguageJob> languageJobs = new ArrayList<>();

        for (int i = 0; i < jobRequest.getLanguages().size(); i++) {
            LanguageJob lj = languageJobRepository.getByLanguageIdAndJobId(jobRequest.getLanguages().get(i).getLanguageId(),
                    job.getId());

            if (!Validator.isValidObject(lj)){
                LanguageJob langJob = new LanguageJob();

                langJob.setId(UniqueID.getUUID());
                langJob.setLanguageId(jobRequest.getLanguages().get(i).getLanguageId());
                langJob.setJobId(job.getId());
                langJob.setStatus(Status.ACTIVE);
                languageJobs.add(langJob);
            }

        }

        for (int i = 0; i < jobRequest.getSkills().size(); i++) {
            SkillJob sj = skillJobRepository.getBySkillIdAndJobId(jobRequest.getSkills().get(i).getSkillId(),
                    job.getId());
            if (!Validator.isValidObject(sj)){
                SkillJob skillJob = new SkillJob();

                skillJob.setId(UniqueID.getUUID());
                skillJob.setSkillId(jobRequest.getSkills().get(i).getSkillId());
                skillJob.setJobId(job.getId());
                skillJob.setStatus(Status.ACTIVE);
                skillJobs.add(skillJob);
            }
        }

        jobRepository.save(job);
        skillJobRepository.saveAll(skillJobs);
        languageJobRepository.saveAll(languageJobs);
        return job;
    }

    @Override
    public Job updateJob(String id, JobRequest jobRequest) {
        Validator.notNullAndNotEmptyParam(jobRequest.getJobName(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(jobRequest.getJobPosition(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(jobRequest.getCategoryJob(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(jobRequest.getRequired(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(jobRequest.getBenefited(), RestAPIStatus.BAD_PARAMS, "");

        Job job = jobRepository.getById(id);

        Validator.notNull(job, RestAPIStatus.NOT_FOUND, "");

        List<SkillJob> skillJobs = new ArrayList<>();
        List<LanguageJob> languageJobs = new ArrayList<>();

        for (int i = 0; i < jobRequest.getLanguages().size(); i++) {
            if (Validator.isValidParam(jobRequest.getLanguages().get(i).getId())){
                if (Validator.mustEquals(jobRequest.getLanguages().get(i).getStatus(), Status.IN_ACTIVE)) {
                    languageJobRepository.deleteById(jobRequest.getLanguages().get(i).getLanguageId());
                } else {
                    LanguageJob languageJob = languageJobRepository.
                            getById(jobRequest.getLanguages().get(i).getLanguageId());
                    languageJobs.add(languageJob);
                }
            }

            if (!Validator.isValidParam(jobRequest.getLanguages().get(i).getId()) &&
                    Validator.mustEquals(jobRequest.getLanguages().get(i).getStatus(), Status.ACTIVE)) {

                LanguageJob langJob = new LanguageJob();

                langJob.setId(UniqueID.getUUID());
                langJob.setLanguageId(jobRequest.getLanguages().get(i).getLanguageId());
                langJob.setJobId(job.getId());
                langJob.setStatus(Status.ACTIVE);
                languageJobs.add(langJob);
            }
        }

        for (int i = 0; i < jobRequest.getSkills().size(); i++) {
            if (Validator.isValidParam(jobRequest.getSkills().get(i).getId())){
                if (Validator.mustEquals(jobRequest.getSkills().get(i).getStatus(), Status.IN_ACTIVE)) {
                    skillJobRepository.deleteById(jobRequest.getSkills().get(i).getSkillId());
                } else {
                    SkillJob skillJob = skillJobRepository.
                            getById(jobRequest.getSkills().get(i).getSkillId());
                    skillJobs.add(skillJob);
                }
            }

            if (!Validator.isValidParam(jobRequest.getSkills().get(i).getId()) &&
                    Validator.mustEquals(jobRequest.getSkills().get(i).getStatus(), Status.ACTIVE)) {

                SkillJob skillJob = new SkillJob();

                skillJob.setId(UniqueID.getUUID());
                skillJob.setSkillId(jobRequest.getSkills().get(i).getSkillId());
                skillJob.setJobId(job.getId());
                skillJob.setStatus(Status.ACTIVE);
                skillJobs.add(skillJob);
            }
        }

        jobRepository.save(editJob(job, jobRequest));
        skillJobRepository.saveAll(skillJobs);
        languageJobRepository.saveAll(languageJobs);
        return job;
    }

    @Override
    public JobResponse getJobById(String id) throws IOException {
        Job job = jobRepository.getById(id);
        Validator.notNull(job, RestAPIStatus.NOT_FOUND, "");
        Company c = companyRepository.getById(job.getCompanyId());
        List<LangJobResponse> langJobResponses = languageJobRepository.getAllByJobId(job.getId());
        List<SkillJobResponse> skillJobs = skillJobRepository.getAllByJobId(job.getId());
        return new JobResponse(job, AppUtil.getUrlCompany(c), c.getCompanyName(), c.getId(),
                skillJobs, langJobResponses);
    }

    @Override
    public List<JobResponse> getJobsByCompanyId(String companyId) {
        Company company = companyRepository.getById(companyId);
        Validator.notNull(company, RestAPIStatus.NOT_FOUND, "");
        List<Job> jobs = jobRepository.getAllByCompanyId(companyId);

        try {
            String url = AppUtil.getUrlCompany(company);

            List<JobResponse> jobResponses = new ArrayList<>();
            for (int i = 0; i < jobs.size(); i++) {
                List<SkillJobResponse> skills = skillJobRepository.getAllByJobId(jobs.get(i).getId());
                List<LangJobResponse> langs = languageJobRepository.getAllByJobId(jobs.get(i).getId());
                jobResponses.add(new JobResponse(jobs.get(i), url, company.getCompanyName(),
                        jobs.get(i).getCompanyId(), skills, langs));
            }

            return jobResponses;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<JobResponse> getAllJobs() {
        List<Company> c = companyRepository.findAll();
        List<JobResponse> jobs = new ArrayList<>();
        for (int i = 0; i < c.size(); i++) {
            jobs.addAll(getJobsByCompanyId(c.get(i).getId()));
        }

        return jobs;
    }

    @Override
    public Page<JobResponse> getPageAllJobs(int pageNumber, int pageSize) {
        PageRequest request = PageRequest.of(pageNumber - 1, pageSize);
        return jobRepository.getAllJobs(request);
    }

    @Override
    public List<JobResponse> getListJobsRecommend(String major) {
        List<Company> c = companyRepository.findAll();
        List<JobResponse> jobs = new ArrayList<>();
        for (Company company : c) {
            jobs.addAll(getJobsByCompanyIdAndCategoryJob(company.getId(), major));
        }

        return jobs;
    }

    @Override
    public List<JobResponse> searchJobs(String searchKey) {
        List<JobResponse> jobs = new ArrayList<JobResponse>();
        List<Company> companies = companyRepository.findAll();
        try {
            Map<String, String> map = new HashMap<>();

            for (Company company : companies) {
                    map.put(company.getId(),
                            AppUtil.getUrlCompany(company));
            }

            if (Validator.isValidParam(searchKey)) {
                List<JobResponse> j = jobRepository.getBySearchKey(searchKey);

                for (JobResponse jobResponse : j) {
                    String url = map.get(jobResponse.getCompanyId());
                    jobs.add(new JobResponse(jobResponse, url));
                }
            } else {
                List<JobResponse> j = jobRepository.getAllJobs();

                for (JobResponse jobResponse : j) {
                    String url = map.get(jobResponse.getCompanyId());
                    jobs.add(new JobResponse(jobResponse, url));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return jobs;
    }

    @Override
    public String deleteJob(String id) {
        Job job = jobRepository.getById(id);
        Validator.notNull(job, RestAPIStatus.NOT_FOUND, "");

        List<UserJob> userJob = userJobRepository.getAllByJobId(id);
        List<SkillJob> skillJobs = skillJobRepository.findAllByJobId(id);
        List<LanguageJob> languageJobs = languageJobRepository.findAllByJobId(id);

        if (Validator.isValidObject(userJob)) {
            userJobRepository.deleteAll(userJob);
        }

        if (Validator.isValidObject(skillJobs)) {
            skillJobRepository.deleteAll(skillJobs);
        }

        if (Validator.isValidObject(languageJobs)) {
            languageJobRepository.deleteAll(languageJobs);
        }

        jobRepository.delete(job);
        return "Delete successfully!";
    }

    private static Job newJob(JobRequest jobRequest) {
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
        job.setAddress(jobRequest.getAddress());
        return job;
    }

    private static Job editJob(Job job, JobRequest jobRequest) {
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
        job.setAddress(jobRequest.getAddress());
        return job;
    }

    public List<JobResponse> getJobsByCompanyIdAndCategoryJob(String companyId, String major) {
        Company company = companyRepository.getById(companyId);
        Validator.notNull(company, RestAPIStatus.NOT_FOUND, "");
        List<Job> jobs = jobRepository.getAllByCompanyId(companyId, major);

        try {
            String url = AppUtil.getUrlCompany(company);

            List<JobResponse> jobResponses = new ArrayList<>();
            for (int i = 0; i < jobs.size(); i++) {
                List<SkillJobResponse> skills = skillJobRepository.getAllByJobId(jobs.get(i).getId());
                List<LangJobResponse> langs = languageJobRepository.getAllByJobId(jobs.get(i).getId());
                jobResponses.add(new JobResponse(jobs.get(i), url, company.getCompanyName(),
                        jobs.get(i).getCompanyId(), skills, langs));
            }

            return jobResponses;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

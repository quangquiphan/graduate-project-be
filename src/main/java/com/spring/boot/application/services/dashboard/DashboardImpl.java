package com.spring.boot.application.services.dashboard;

import com.spring.boot.application.controller.model.response.dashboard.DashboardResponse;
import com.spring.boot.application.repositories.CompanyRepository;
import com.spring.boot.application.repositories.JobRepository;
import com.spring.boot.application.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardImpl implements DashboardService{
    final private UserRepository userRepository;
    final private JobRepository jobRepository;
    final private CompanyRepository companyRepository;

    public DashboardImpl(
            UserRepository userRepository,
            JobRepository jobRepository,
            CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public DashboardResponse getDashboard() {
        return new DashboardResponse(userRepository.countCandidates(), jobRepository.countJobs(),
                companyRepository.countCompanies());
    }
}

package com.spring.boot.application.controller.model.response.company;

import com.spring.boot.application.controller.model.response.job.JobResponse;
import com.spring.boot.application.entity.Company;
import com.spring.boot.application.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse {
    private String id;
    private String avatar;
    private String background;
    private String companyName;
    private String email;
    private String address;
    private String phoneNumber;
    private String overview;
    private String size;
    private String website;

    public CompanyResponse(Company company, String avatarUrl, String bgUrl) {
        this.id = company.getId();
        this.avatar = avatarUrl;
        this.background = bgUrl;
        this.companyName = company.getCompanyName();
        this.email = company.getEmail();
        this.address =company.getAddress();
        this.phoneNumber = company.getPhoneNumber();
        this.overview = company.getOverview();
        this.size = company.getSize();
        this.website = company.getWebsite();
    }
}

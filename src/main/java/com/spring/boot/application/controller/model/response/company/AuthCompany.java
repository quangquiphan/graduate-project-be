package com.spring.boot.application.controller.model.response.company;

import com.spring.boot.application.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthCompany {
    private String id;
    private String avatar;
    private String companyName;
    private String email;
    private String address;
    private String phoneNumber;
    private String overview;
    private String size;
    private String website;

    public AuthCompany(Company company, String avatarUrl) {
        this.id = company.getId();
        this.avatar = avatarUrl;
        this.companyName = company.getCompanyName();
        this.email = company.getEmail();
        this.address =company.getAddress();
        this.phoneNumber = company.getPhoneNumber();
        this.overview = company.getOverview();
        this.size = company.getSize();
        this.website = company.getWebsite();
    }
}

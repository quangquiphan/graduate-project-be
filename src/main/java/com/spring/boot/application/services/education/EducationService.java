package com.spring.boot.application.services.education;

import com.spring.boot.application.controller.model.request.education.EducationRequest;
import com.spring.boot.application.entity.Education;

public interface EducationService {
    Education addEducation(EducationRequest addEducation);
    Education getEducation(String id);
    Education updateEducation(String id, EducationRequest updateEducation);
    String deleteEducation(String id);
}

package com.spring.boot.application.services.education;

import com.spring.boot.application.controller.model.request.education.AddEducation;
import com.spring.boot.application.entity.Education;

public interface EducationService {
    Education addEducation(AddEducation addEducation);
    Education updateEducation(String id, AddEducation updateEducation);
    String deleteEducation(String id);
}

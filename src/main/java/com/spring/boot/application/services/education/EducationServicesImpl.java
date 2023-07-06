package com.spring.boot.application.services.education;

import com.spring.boot.application.common.utils.RestAPIStatus;
import com.spring.boot.application.common.utils.UniqueID;
import com.spring.boot.application.common.utils.Validator;
import com.spring.boot.application.controller.model.request.education.AddEducation;
import com.spring.boot.application.entity.Education;
import com.spring.boot.application.repositories.EducationRepository;
import org.springframework.stereotype.Service;

@Service
public class EducationServicesImpl implements EducationService{
    final private EducationRepository educationRepository;

    public EducationServicesImpl(EducationRepository educationRepository) {
        this.educationRepository = educationRepository;
    }

    @Override
    public Education addEducation(AddEducation addEducation) {
        Validator.notNullAndNotEmptyParam(addEducation.getUserId(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(addEducation.getSchoolName(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(addEducation.getCourse(), RestAPIStatus.BAD_PARAMS, "");

        Education education = new Education();
        education.setId(UniqueID.getUUID());
        education.setSchoolName(addEducation.getSchoolName());
        education.setMajor(addEducation.getCourse());
        education.setFromDate(addEducation.getFromDate());
        education.setCurrent(addEducation.isCurrent());

        if (addEducation.isCurrent())
            education.setToDate(null);
        else education.setToDate(addEducation.getToDate());

        education.setDescription(addEducation.getDescription());
        education.setUserId(addEducation.getUserId());

        return educationRepository.save(education);
    }

    @Override
    public Education updateEducation(String id, AddEducation updateEducation) {
        Validator.notNullAndNotEmptyParam(updateEducation.getUserId(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(updateEducation.getSchoolName(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(updateEducation.getCourse(), RestAPIStatus.BAD_PARAMS, "");

        Education education = educationRepository.getById(id);
        Validator.notNullAndNotEmpty(education, RestAPIStatus.NOT_FOUND, "");

        education.setSchoolName(updateEducation.getSchoolName());
        education.setMajor(updateEducation.getCourse());
        education.setFromDate(updateEducation.getFromDate());
        education.setCurrent(updateEducation.isCurrent());

        if (updateEducation.isCurrent())
            education.setToDate(null);
        else education.setToDate(updateEducation.getToDate());

        education.setDescription(updateEducation.getDescription());
        education.setUserId(updateEducation.getUserId());

        return educationRepository.save(education);
    }

    @Override
    public String deleteEducation(String id) {
        Education education = educationRepository.getById(id);
        Validator.notNullAndNotEmpty(education, RestAPIStatus.NOT_FOUND, "");

        educationRepository.delete(education);
        return "Delete successfully!";
    }
}

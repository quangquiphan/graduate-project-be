package com.spring.boot.application.services.education;

import com.spring.boot.application.common.utils.RestAPIStatus;
import com.spring.boot.application.common.utils.UniqueID;
import com.spring.boot.application.common.utils.Validator;
import com.spring.boot.application.controller.model.request.education.EducationRequest;
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
    public Education addEducation(EducationRequest addEducation) {
        Validator.notNullAndNotEmptyParam(addEducation.getUserId(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(addEducation.getSchoolName(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(addEducation.getCourse(), RestAPIStatus.BAD_PARAMS, "");

        return educationRepository.save(newEducation(addEducation));
    }

    @Override
    public Education getEducation(String id) {
        return educationRepository.getById(id);
    }

    @Override
    public Education updateEducation(String id, EducationRequest updateEducation) {
        Validator.notNullAndNotEmptyParam(updateEducation.getUserId(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(updateEducation.getSchoolName(), RestAPIStatus.BAD_PARAMS, "");
        Validator.notNullAndNotEmptyParam(updateEducation.getCourse(), RestAPIStatus.BAD_PARAMS, "");

        Education education = educationRepository.getById(id);
        Validator.notNullAndNotEmpty(education, RestAPIStatus.NOT_FOUND, "");

        return educationRepository.save(upEducation(education, updateEducation));
    }

    @Override
    public String deleteEducation(String id) {
        Education education = educationRepository.getById(id);
        Validator.notNullAndNotEmpty(education, RestAPIStatus.NOT_FOUND, "");

        educationRepository.delete(education);
        return "Delete successfully!";
    }

    private Education newEducation(EducationRequest addEducation) {
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
        return education;
    }

    private Education upEducation(Education education, EducationRequest addEducation) {
        education.setSchoolName(addEducation.getSchoolName());
        education.setMajor(addEducation.getCourse());
        education.setFromDate(addEducation.getFromDate());
        education.setCurrent(addEducation.isCurrent());

        if (addEducation.isCurrent())
            education.setToDate(null);
        else education.setToDate(addEducation.getToDate());

        education.setDescription(addEducation.getDescription());
        return education;
    }
}

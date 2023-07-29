package com.spring.boot.application.services.skill;

import com.spring.boot.application.common.utils.RestAPIStatus;
import com.spring.boot.application.common.utils.UniqueID;
import com.spring.boot.application.common.utils.Validator;
import com.spring.boot.application.controller.model.request.skill.LanguageRequest;
import com.spring.boot.application.entity.Language;
import com.spring.boot.application.repositories.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageServiceImpl implements LanguageService{
    final private LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public Language addLanguage(LanguageRequest language) {
        Validator.notNullAndNotEmptyParam(language.getLanguage(), RestAPIStatus.BAD_PARAMS, "");
        Language existLanguage = languageRepository.getByLanguage(language.getLanguage());
        Validator.mustNull(existLanguage, RestAPIStatus.EXISTED, "");
        Language l = new Language();
        l.setId(UniqueID.getUUID());
        l.setLanguage(language.getLanguage());

        return languageRepository.save(l);
    }

    @Override
    public Language updateLanguage(String id, LanguageRequest language) {
        Language l = languageRepository.getById(id);
        Validator.notNullAndNotEmpty(l, RestAPIStatus.NOT_FOUND, "");
        Validator.notNullAndNotEmptyParam(language.getLanguage(), RestAPIStatus.BAD_PARAMS, "");

        Language existLanguage = languageRepository.getByLanguage(language.getLanguage());
        Validator.mustNull(existLanguage, RestAPIStatus.EXISTED, "");

        l.setLanguage(language.getLanguage());
        return languageRepository.save(l);
    }

    @Override
    public List<Language> getAllLanguage() {
        return languageRepository.getAll();
    }

    @Override
    public String deleteLanguage(String id) {
        Language l = languageRepository.getById(id);
        Validator.notNullAndNotEmpty(l, RestAPIStatus.NOT_FOUND, "");

        languageRepository.delete(l);
        return "Delete successfully!";
    }
}

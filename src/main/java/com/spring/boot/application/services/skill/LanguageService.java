package com.spring.boot.application.services.skill;

import com.spring.boot.application.controller.model.request.skill.LanguageRequest;
import com.spring.boot.application.entity.Language;

import java.util.List;

public interface LanguageService {
    Language addLanguage(LanguageRequest language);
    Language updateLanguage(String id, LanguageRequest language);
    List<Language> getAllLanguage();
    String deleteLanguage(String id);
}

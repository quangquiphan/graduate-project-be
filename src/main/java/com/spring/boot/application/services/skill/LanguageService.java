package com.spring.boot.application.services.skill;

import com.spring.boot.application.controller.model.request.skill.AddLanguage;
import com.spring.boot.application.entity.Language;

import java.util.List;

public interface LanguageService {
    Language addLanguage(AddLanguage language);
    Language updateLanguage(String id, AddLanguage language);
    List<Language> getAllLanguage();
    String deleteLanguage(String id);
}

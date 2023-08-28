package com.spring.boot.application.services.skill;

import com.spring.boot.application.controller.model.request.skill.LanguageRequest;
import com.spring.boot.application.entity.Language;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LanguageService {
    Language addLanguage(LanguageRequest language);
    Language getLang(String id);
    Language updateLanguage(String id, LanguageRequest language);
    List<Language> getAllLanguage();
    Page<Language> getPageLanguage(int pageNumber, int pageSize);
    Page<Language> searchLanguage(String keyword, int pageNumber, int pageSize);
    String deleteLanguage(String id);
}

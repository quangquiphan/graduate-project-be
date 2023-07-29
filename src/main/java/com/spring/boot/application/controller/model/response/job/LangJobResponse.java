package com.spring.boot.application.controller.model.response.job;

import com.spring.boot.application.entity.Language;
import com.spring.boot.application.entity.LanguageJob;
import com.spring.boot.application.entity.Skill;
import com.spring.boot.application.entity.SkillJob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LangJobResponse {
    private String id;
    private String name;
    public LangJobResponse(LanguageJob lj, Language l) {
        this.id = lj.getId();
        this.name = l.getLanguage();
    }
}

package com.spring.boot.application.controller.model.response.skill;

import com.spring.boot.application.common.enums.Status;
import com.spring.boot.application.entity.Language;
import com.spring.boot.application.entity.UserLang;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLangResponse {
    private String id;
    private String langId;
    private String name;
    private Status status;

    public UserLangResponse(UserLang ul, Language l) {
        this.id = ul.getId();
        this.langId = l.getId();
        this.name = l.getLanguage();
        this.status = ul.getStatus();
    }
}

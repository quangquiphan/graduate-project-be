package com.spring.boot.application.controller.model.response.skill;

import com.spring.boot.application.common.enums.Status;
import com.spring.boot.application.entity.Skill;
import com.spring.boot.application.entity.UserSkill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSkillResponse {
    private String id;
    private String skillId;
    private String name;
    private Status status;

    public UserSkillResponse(UserSkill us, Skill s) {
        this.id = us.getId();
        this.skillId = s.getId();
        this.name = s.getSkillName();
        this.status = us.getStatus();
    }
}

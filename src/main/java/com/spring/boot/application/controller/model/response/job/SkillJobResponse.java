package com.spring.boot.application.controller.model.response.job;

import com.spring.boot.application.entity.Skill;
import com.spring.boot.application.entity.SkillJob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillJobResponse {
    private String id;
    private String name;
    public SkillJobResponse(SkillJob sj, Skill s) {
        this.id = sj.getId();
        this.name = s.getSkillName();
    }
}

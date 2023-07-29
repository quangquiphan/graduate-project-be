package com.spring.boot.application.services.experience;

import com.spring.boot.application.controller.model.request.experience.ProjectRequest;
import com.spring.boot.application.entity.Project;

public interface ProjectService {
    Project getProject(String id);
    Project updateProject(String id, ProjectRequest project);
    String deleteProject(String id);
}

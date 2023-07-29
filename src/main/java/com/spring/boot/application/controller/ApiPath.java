package com.spring.boot.application.controller;

public interface ApiPath {
    String BASE_API = "/api";

    String AUTHENTICATED_APIs = BASE_API + "/authenticated";

    String USER_APIs = BASE_API + "/user";

    String EDUCATION_APIs = BASE_API + "/education";
    String PROJECT_APIs = BASE_API + "/project";
    String WORK_HISTORY_APIs = BASE_API + "/work-history";

    String CANDIDATE_APIs = BASE_API + "/candidate";

    String SKILL_APIs = BASE_API + "/skill";

    String LANGUAGE_APIs = BASE_API + "/language";

    String COMPANY_APIs = BASE_API + "/company";

    String JOB_APIs = BASE_API + "/job";
}

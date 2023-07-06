package com.spring.boot.application.services.mail.thymleaf;

import java.util.Map;

public interface ThymeleafService {

    String createdContent(String template, Map<String, Object> variables);
}

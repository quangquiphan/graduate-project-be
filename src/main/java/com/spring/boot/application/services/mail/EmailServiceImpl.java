package com.spring.boot.application.services.mail;

import com.spring.boot.application.entity.Company;
import com.spring.boot.application.entity.User;
import com.spring.boot.application.services.mail.thymleaf.ThymeleafService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ThymeleafService thymeleafService;

    @Value("${spring.mail.username}")
    private String email;

    @Override
    public void confirmRegisterAccount(User user) {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("first_name", user.getFirstName());
            variables.put("last_name", user.getLastName());
            variables.put("active_code", user.getActiveCode());

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.setFrom(email);
            helper.setTo(user.getEmail());
            helper.setSubject("Verify email");


            helper.setText(thymeleafService.createdContent("confirm-register-account.html", variables), true);

            mailSender.send(message);
            System.out.println("done ....");
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void addAccountCompany(Company company) {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("company_name", company.getCompanyName());

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.setFrom(email);
            helper.setTo(company.getEmail());
            helper.setSubject("Create account company");


            helper.setText(thymeleafService.createdContent("add-company-account.html", variables), true);

            mailSender.send(message);
            System.out.println("done ....");
        } catch (Exception e) {
            e.getMessage();
        }
    }
}

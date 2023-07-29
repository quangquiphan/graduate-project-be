package com.spring.boot.application.config.jwt;

import com.spring.boot.application.common.auth.AuthUser;
import com.spring.boot.application.common.exceptions.ApplicationException;
import com.spring.boot.application.common.utils.AppUtil;
import com.spring.boot.application.common.utils.RestAPIStatus;
import com.spring.boot.application.common.utils.Validator;
import com.spring.boot.application.controller.model.response.company.AuthCompany;
import com.spring.boot.application.entity.Company;
import com.spring.boot.application.entity.User;
import com.spring.boot.application.repositories.CompanyRepository;
import com.spring.boot.application.repositories.UserRepository;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenUtil {
    private final String JWT_SECRET = "0123456789abcdefghijklmnOPQRSTUVWXYZ!@#$%^&*()";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000;

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setIssuer(user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();

    }

    public AuthUser getUserIdFromJWT(String token) {
        validateToken(token);

        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        User user = userRepository.getById(claims.getIssuer());

        if (!user.getCompanyId().isEmpty()) {
            Company company = companyRepository.getById(user.getCompanyId());
            Validator.notNull(company, RestAPIStatus.NOT_FOUND, "");
            try {
                return new AuthUser(user,
                        new AuthCompany(company, AppUtil.getUrlCompany(company, true),
                                AppUtil.getUrlCompany(company, false)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return new AuthUser(user);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "JWT claims string is empty.");
        }
    }
}

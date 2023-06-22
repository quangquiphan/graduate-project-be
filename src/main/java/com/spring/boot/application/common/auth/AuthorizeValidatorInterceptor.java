package com.spring.boot.application.common.auth;

import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.common.exceptions.ApplicationException;
import com.spring.boot.application.common.utils.Constant;
import com.spring.boot.application.common.utils.RestAPIStatus;
import com.spring.boot.application.config.jwt.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class AuthorizeValidatorInterceptor {
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Before(value = "@annotation(com.spring.boot.application.common.auth.AuthorizeValidator)  && @annotation(roles)")
    public void before(JoinPoint caller, AuthorizeValidator roles) {
        // Capture access token from current request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String token = request.getHeader(Constant.HEADER_TOKEN);
        // Check Get current Authenticate user from access token
        AuthUser authUser = jwtTokenUtil.getUserIdFromJWT(token);
        if (authUser == null)
            throw new ApplicationException(RestAPIStatus.UNAUTHORIZED);
        // Validate Role
        boolean isValid = isValidate(authUser, roles);
        if (!isValid)
            throw new ApplicationException(RestAPIStatus.FORBIDDEN);
    }

    public boolean isValidate(AuthUser authUser, AuthorizeValidator roles) {
        for (UserRole userRole : roles.value()) {
            if(userRole == authUser.getRole())
                return true;
        }

        return false;
    }
}

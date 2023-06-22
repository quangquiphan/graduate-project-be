package com.spring.boot.application.services.session;

import com.spring.boot.application.common.auth.AuthUser;
import com.spring.boot.application.common.enums.Status;
import com.spring.boot.application.common.exceptions.ApplicationException;
import com.spring.boot.application.common.utils.Constant;
import com.spring.boot.application.common.utils.DateUtil;
import com.spring.boot.application.common.utils.RestAPIStatus;
import com.spring.boot.application.common.utils.Validator;
import com.spring.boot.application.config.jwt.JwtTokenUtil;
import com.spring.boot.application.controller.model.request.auth.SignIn;
import com.spring.boot.application.entity.Session;
import com.spring.boot.application.entity.User;
import com.spring.boot.application.repositories.SessionRepository;
import com.spring.boot.application.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SessionServiceImpl implements SessionService {
    final private SessionRepository sessionRepository;
    final private UserRepository userRepository;
    final private JwtTokenUtil jwtTokenUtil;

    public SessionServiceImpl(
            SessionRepository sessionRepository,
            UserRepository userRepository,
            JwtTokenUtil jwtTokenUtil
    ) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.API_FORMAT_DATE);

    @Override
    public Session signIn(SignIn signIn, PasswordEncoder passwordEncoder) {
        User user = userRepository.getByEmailAndStatus(signIn.getEmail(), Status.ACTIVE);

        // Check valid params
        Validator.notNullAndNotEmpty(user, RestAPIStatus.NOT_FOUND, "User not found");
        Validator.notNullAndNotEmptyParam(signIn.getEmail(), RestAPIStatus.BAD_PARAMS, "Email not null");
        Validator.validEmailAddressRegex(signIn.getEmail(), RestAPIStatus.BAD_PARAMS, "Email invalid");

        boolean isMatches = passwordEncoder.matches(signIn.getPasswordHash().trim()
                                                        .concat(user.getPasswordSalt().trim()),
                                                         user.getPasswordHash());

        if (!isMatches) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Wrong password");
        }

        Session session = new Session();
        session.setAccessToken(jwtTokenUtil.generateAccessToken(user));
        session.setUserId(user.getId());
        session.setCreatedDate(DateUtil.convertToUTC(new Date()));

        if (signIn.isKeepLogin()) {
            try {
                session.setExpiryDate(dateFormat.parse("12/31/9999 00:00:00"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            session.setExpiryDate(DateUtil.addHoursToJavaUtilDate(new Date(), 24));
        }
        return sessionRepository.save(session);
    }

    @Override
    public String signOut(String token) {
        Session session = sessionRepository.getByAccessToken(token);
        Validator.notNullAndNotEmpty(session, RestAPIStatus.NOT_FOUND, "");
        sessionRepository.delete(session);
        return "Sign out successfully!";
    }
}

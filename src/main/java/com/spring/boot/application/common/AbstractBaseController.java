package com.spring.boot.application.common;

import com.spring.boot.application.common.utils.Constant;
import com.spring.boot.application.common.utils.ResponseUtil;
import com.spring.boot.application.config.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;

public abstract class AbstractBaseController {
    @Autowired
    public ResponseUtil responseUtil;

    @Autowired
    public JwtTokenUtil jwtTokenUtil;

    public final SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
}

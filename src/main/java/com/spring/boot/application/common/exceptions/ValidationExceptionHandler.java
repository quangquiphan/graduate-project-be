package com.spring.boot.application.common.exceptions;

import com.spring.boot.application.common.utils.ResponseUtil;
import com.spring.boot.application.common.utils.RestAPIResponse;
import com.spring.boot.application.common.utils.RestAPIStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
@Slf4j
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private ResponseUtil responseUtil;

    @ExceptionHandler(value = ApplicationException.class)
    public ResponseEntity<RestAPIResponse> handleApplicationException(ApplicationException ex, WebRequest request) {

//        log.debug("handleApplicationException", ex);

        ResponseEntity<RestAPIResponse> response;
        if (ex.getApiStatus() == RestAPIStatus.BAD_REQUEST) {
            // handle bad request
            response = responseUtil.buildResponse(RestAPIStatus.BAD_REQUEST, ex.getMessage(), HttpStatus.MULTI_STATUS);
        } else {
            response = responseUtil.buildResponse(ex.getApiStatus(), ex.getMessage(), HttpStatus.OK);
        }

        return response;
    }

    // when missing parameter
//    @Override
//    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
//                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        return new ResponseEntity<Object>(new RestAPIResponse(RestAPIStatus.BAD_REQUEST, ex.getMessage()), headers, status);
//    }
//
//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<RestAPIResponse> handleUncatchException(Exception ex, WebRequest request) {
//
////        log.error("handleUncatchException", ex);
//        return responseUtil.buildResponse(RestAPIStatus.INTERNAL_SERVER_ERROR, "Please contact System SysAdmin to resolve problem", HttpStatus.INTERNAL_SERVER_ERROR);
//    }

//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
//        final String FIELD_NAME_PATTERN = "\\{fieldName}";
//        StringBuilder sb = new StringBuilder();
//        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
//        for(FieldError fieldError: fieldErrors){
//            String message = "";
//            if(fieldError.getDefaultMessage() != null){
//                message = fieldError.getDefaultMessage();
//                message = message.replaceFirst(FIELD_NAME_PATTERN, fieldError.getField());
//            }
//
//            sb.append(message).append("; ");
//        }
//
//        return new ResponseEntity<Object>(new RestAPIResponse(RestAPIStatus.BAD_REQUEST, sb.toString()), headers, status);
//    }

}

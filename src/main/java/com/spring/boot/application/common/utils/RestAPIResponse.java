package com.spring.boot.application.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.io.Serializable;

/**
 * 
 * @author Digiex
 * @param <T> 
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestAPIResponse<T extends Object> implements Serializable {

    /**
     * status & message fields have not setter. They are assigned value when
     * initial by APIStatus parameter
     */
    private int status;
    private String message;
    private T data;
    private String description;

    public RestAPIResponse(RestAPIStatus restApiStatus, T data) {

        if (restApiStatus == null) {
            throw new IllegalArgumentException("APIStatus must not be null");
        }

        this.status = restApiStatus.getCode();
        this.message = restApiStatus.getDescription();
        this.data = data;
        this.description = "";
    }

    public RestAPIResponse(RestAPIStatus restApiStatus, T data, String description) {

        if (restApiStatus == null) {
            throw new IllegalArgumentException("APIStatus must not be null");
        }

        this.status = restApiStatus.getCode();
        this.message = restApiStatus.getDescription();
        this.data = data;
        this.description = description;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

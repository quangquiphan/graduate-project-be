package com.spring.boot.application.common.utils;

import com.spring.boot.application.common.exceptions.ApplicationException;

import java.util.List;
import java.util.regex.Pattern;

public class Validator {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX
            = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);

    /**
     * @param message
     * @throws ApplicationException if {@code obj} is NOT null
     */

    public static void mustNull(Object obj, RestAPIStatus RestAPIStatus, String message) {

        if (obj != null) {
            throw new ApplicationException(RestAPIStatus, message);
        }
    }


    public static void notNull(Object obj, RestAPIStatus RestAPIStatus, String message) {
        if (obj == null) {
            throw new ApplicationException(RestAPIStatus, message);
        }
    }

    /**
     * Validate list object not null & not empty
     *
     * @param obj
     * @param message
     */

    public static void notNullAndNotEmpty(List<?> obj, RestAPIStatus RestAPIStatus, String message) {

        if (obj == null || obj.isEmpty()) {
            throw new ApplicationException(RestAPIStatus, message);
        }
    }

    /**
     * Validate object not null & not empty
     *
     * @param obj
     * @param message
     */

    public static void notNullAndNotEmpty(Object obj, RestAPIStatus RestAPIStatus, String message) {

        if (obj == null || "".equals(obj)) {
            throw new ApplicationException(RestAPIStatus, message);
        }
    }


    /**
     * Validate list object must null & must empty
     *
     * @param obj
     * @param apiStatus
     * @param message
     */
    public static void mustNullAndMustEmpty(List<?> obj, RestAPIStatus apiStatus, String message) {
        if (obj != null && !obj.isEmpty()) {
            throw new ApplicationException(apiStatus, message);
        }
    }


    public static void mustEquals(String str1, String str2, RestAPIStatus RestAPIStatus, String message) {
        if (!str1.equals(str2)) {
            throw new ApplicationException(RestAPIStatus, message);

        }
    }

    public static void notNullAndNotEmptyParam(String param, RestAPIStatus restAPIStatus, String message) {
        if (param.isEmpty() && param.equals("")) {
            throw new ApplicationException(restAPIStatus, message);
        }
    }

    public static void validEmailAddressRegex(String email, RestAPIStatus restAPIStatus, String message){
        String regexPattern = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$";

        if (!Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE).matcher(email).matches()) {
            throw new ApplicationException(restAPIStatus, message);
        }
    }

    public static boolean isValidObject(List<?> list) {
        if (list.isEmpty() || list.equals("") || list.size() == 0)
            return false;
        return true;
    }

    public static boolean isValidObject(Object obj) {
        if ("".equals(obj) || obj == null)
            return false;
        return true;
    }

    public static boolean isValidParam(String param) {
        if ("".equals(param) || param == null)
            return false;
        return true;
    }
}

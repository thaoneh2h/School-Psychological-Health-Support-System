package com.be.custom.dto.response_api;

public interface ResponseCase { //todo same with package com.be.base.dto -> move to com.be.base.dto
    //common
    ResponseStatus SUCCESS = new ResponseStatus(1000, "SUCCESS");
    ResponseStatus ERROR = new ResponseStatus(4, "ERROR");
    ResponseStatus USER_NOT_LOGIN = new ResponseStatus(1401, "USER NOT LOGIN");
    ResponseStatus NOT_FOUND = new ResponseStatus(1404, "NOT FOUND");

    // patientInfo
    ResponseStatus NOT_FOUND_PATIENT_INFO = new ResponseStatus(1405, "PATIENT INFO NOT FOUND");
    ResponseStatus INVALID_PATIENT_INFO_PARAM = new ResponseStatus(5000, "invalid patient info param");

    //login
    ResponseStatus INVALID_WEB_LOGIN_PARAM = new ResponseStatus(1406, "Invalid web login param!");
    ResponseStatus INVALID_WEB_PASSWORD = new ResponseStatus(1407, "Invalid web login password param!");

    //changePassword
    ResponseStatus INVALID_NEW_PASSWORD = new ResponseStatus(1408, "Invalid web password");
    ResponseStatus OLD_PASSWORD_IS_INCORRECT = new ResponseStatus(1409, "old password is incorrect");

}

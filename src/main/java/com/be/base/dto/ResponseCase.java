package com.be.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResponseCase {

    SUCCESS(1000, "SUCCESS"),
    ERROR(1004, "ERROR"),

    USERNAME_PWD_INVALID(1005, "USERNAME_PASSWORD_INVALID");

    private final int code;
    private final String message;

    ResponseCase(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

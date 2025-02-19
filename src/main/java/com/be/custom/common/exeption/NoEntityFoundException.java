package com.be.custom.common.exeption;

public class NoEntityFoundException extends RuntimeException {

    private NoEntityFoundException(String message) {
        super(message);
    }

    public static NoEntityFoundException with(String msg) {
        return new NoEntityFoundException(msg);
    }

    public static NoEntityFoundException emptyMsg() {
        return new NoEntityFoundException("");
    }
}

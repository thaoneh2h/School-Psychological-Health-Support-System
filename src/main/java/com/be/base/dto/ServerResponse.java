package com.be.base.dto;

import lombok.Getter;

@Getter
public class ServerResponse {

    public static final ServerResponse SUCCESS = new ServerResponse(ResponseCase.SUCCESS);
    public static final ServerResponse ERROR = new ServerResponse(ResponseCase.ERROR);

    private final ResponseCase status;
    private Object data;

    private ServerResponse(ResponseCase responseCase) {
        this.status = responseCase;
    }

    private ServerResponse(ResponseCase responseCase, Object data) {
        this.status = responseCase;
        this.data = data;
    }

    public static ServerResponse success(Object data) {
        return new ServerResponse(ResponseCase.SUCCESS, data);
    }

    public static ServerResponse error(Object data) {
        return new ServerResponse(ResponseCase.ERROR, data);
    }

    public static ServerResponse with(ResponseCase responseCase) {
        return new ServerResponse(responseCase);
    }

    public static ServerResponse with(ResponseCase responseCase, Object data) {
        return new ServerResponse(responseCase, data);
    }
}

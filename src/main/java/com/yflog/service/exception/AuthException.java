package com.yflog.service.exception;

/**
 * Created by rooter on 2016/9/4.
 */
public class AuthException extends Exception {

    private final int errCode ;

    public AuthException(int code, String message) {
        super(message);
        this.errCode = code;
    }

    public int getErrCode() {
        return errCode;
    }
}

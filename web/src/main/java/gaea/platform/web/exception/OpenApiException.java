package gaea.platform.web.exception;

import gaea.platform.web.openapi.OpenApiCode;

/**
 * Created by way on 14-5-20.
 */
public class OpenApiException extends RuntimeException {

    private int code;//错误码

    private String message;//错误信息

    private String exception;//错误栈信息

    public OpenApiException(String message) {
        this.code = OpenApiCode.EXCEPTION.getValue();
        this.message = message;
    }

    public OpenApiException(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public OpenApiException(int code, String message, String exception) {
        this.code = code;
        this.message = message;
        this.exception = exception;
    }

    public int getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }

    public String getException() {
        return exception;
    }
}

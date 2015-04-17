package com.nd.gaea.odata.api;

import java.util.Locale;

public class ODataApplicationException extends ODataException {

    private static final long serialVersionUID = 5358683245923127425L;
    private int statusCode = 500;
    private Locale locale;
    private String oDataErrorCode;

    public ODataApplicationException(final String msg, int statusCode, Locale locale) {
        super(msg);
        this.statusCode = statusCode;
        this.locale = locale;
    }

    public ODataApplicationException(final String msg, int statusCode, Locale locale, String oDataErrorCode) {
        super(msg);
        this.statusCode = statusCode;
        this.locale = locale;
        this.oDataErrorCode = oDataErrorCode;
    }

    public ODataApplicationException(final String msg, int statusCode, Locale locale, final Throwable cause) {
        super(msg, cause);
        this.statusCode = statusCode;
        this.locale = locale;
    }

    public ODataApplicationException(final String msg, int statusCode, Locale locale, final Throwable cause,
                                     String oDataErrorCode) {
        super(msg, cause);
        this.statusCode = statusCode;
        this.locale = locale;
        this.oDataErrorCode = oDataErrorCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getODataErrorCode() {
        return oDataErrorCode;
    }
}

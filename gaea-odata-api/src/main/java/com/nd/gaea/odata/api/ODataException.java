package com.nd.gaea.odata.api;

public class ODataException extends Exception {

    private static final long serialVersionUID = 3057981437954048107L;

    public ODataException(final String msg) {
        super(msg);
    }

    public ODataException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public ODataException(final Throwable cause) {
        super(cause);
    }

}

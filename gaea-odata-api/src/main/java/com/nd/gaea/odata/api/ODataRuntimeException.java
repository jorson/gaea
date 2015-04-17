package com.nd.gaea.odata.api;

public class ODataRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 5492375572049190883L;

    public ODataRuntimeException(final String msg) {
        super(msg);
    }

    public ODataRuntimeException(final String msg, final Exception cause) {
        super(msg, cause);
    }

    public ODataRuntimeException(final Exception cause) {
        super(cause);
    }

}

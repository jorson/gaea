package com.nd.gaea.odata.uri.parser;

import com.nd.gaea.odata.api.ODataTranslatedException;

abstract public class UriParserException extends ODataTranslatedException {

    private static final long serialVersionUID = -6438700016830955949L;

    public UriParserException(String developmentMessage, MessageKey messageKey, String... parameters) {
        super(developmentMessage, messageKey, parameters);
    }

    public UriParserException(String developmentMessage, Throwable cause, MessageKey messageKey,
                              String... parameters) {
        super(developmentMessage, cause, messageKey, parameters);
    }

    @Override
    protected String getBundleName() {
        return DEFAULT_SERVER_BUNDLE_NAME;
    }
}

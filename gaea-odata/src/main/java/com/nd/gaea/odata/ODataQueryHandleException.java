package com.nd.gaea.odata;

import com.nd.gaea.odata.api.ODataTranslatedException;

/**
 * Created by Administrator on 2014-11-20.
 */
public class ODataQueryHandleException extends ODataTranslatedException {

    public static enum MessageKeys implements MessageKey {
        /** parameters: HTTP method, HTTP method */ AMBIGUOUS_XHTTP_METHOD,
        /** parameter: HTTP method */ HTTP_METHOD_NOT_IMPLEMENTED,
        /** parameter: processor interface */ PROCESSOR_NOT_IMPLEMENTED,
        FUNCTIONALITY_NOT_IMPLEMENTED,
        /** parameter: version */ ODATA_VERSION_NOT_SUPPORTED;

        @Override
        public String getKey() {
            return name();
        }
    }

    public ODataQueryHandleException(final String developmentMessage, final MessageKey messageKey,
                                 final String... parameters) {
        super(developmentMessage, messageKey, parameters);
    }

    @Override
    protected String getBundleName() {
        return DEFAULT_SERVER_BUNDLE_NAME;
    }
}

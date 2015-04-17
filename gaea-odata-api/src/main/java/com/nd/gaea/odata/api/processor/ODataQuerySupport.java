package com.nd.gaea.odata.api.processor;

import com.nd.gaea.core.repository.query.QuerySupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 在这里输入标题
 * <p/>
 * 说明
 *
 * @author jorson.WHY
 * @package com.nd.gaea.core.odata.processor
 * @since 2014-12-03
 */
public class ODataQuerySupport {

    private final QuerySupport querySupport;
    private final List<Exception> parseExceptions;

    public ODataQuerySupport() {
        querySupport = new QuerySupport();
        parseExceptions = new ArrayList<>();
    }

    public final QuerySupport getQuerySupport() {
        return querySupport;
    }

    public final List<Exception> getParseExceptions() {
        return parseExceptions;
    }
}

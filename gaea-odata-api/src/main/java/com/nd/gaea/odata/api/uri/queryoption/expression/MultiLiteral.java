package com.nd.gaea.odata.api.uri.queryoption.expression;

import java.util.List;

/**
 * Created by Administrator on 2014-12-01.
 */
public interface MultiLiteral {

    /**
     * @return Literal
     */
    public List<String> getItems();

    /**
     * @return Type of the literal if detected
     */
    public Class<?> getType();
}

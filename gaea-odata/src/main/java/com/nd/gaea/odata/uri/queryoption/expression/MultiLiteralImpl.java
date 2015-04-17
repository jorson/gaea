package com.nd.gaea.odata.uri.queryoption.expression;

import com.nd.gaea.odata.api.ODataApplicationException;
import com.nd.gaea.odata.api.uri.queryoption.expression.ExpressionVisitException;
import com.nd.gaea.odata.api.uri.queryoption.expression.ExpressionVisitor;
import com.nd.gaea.odata.api.uri.queryoption.expression.MultiLiteral;

import java.util.List;

/**
 * Created by Administrator on 2014-12-01.
 */
public class MultiLiteralImpl extends ExpressionImpl implements MultiLiteral {

    private List<String> items;
    private Class<?> type;

    @Override
    public List<String> getItems() {
        return items;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    @Override
    public <T> T accept(final ExpressionVisitor<T> visitor) throws ExpressionVisitException, ODataApplicationException {
        return visitor.visitMultiLiteral(items);
    }
}

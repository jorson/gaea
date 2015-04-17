package com.nd.gaea.odata.uri.queryoption.expression;

import com.nd.gaea.odata.api.ODataApplicationException;
import com.nd.gaea.odata.api.uri.queryoption.expression.ExpressionVisitException;
import com.nd.gaea.odata.api.uri.queryoption.expression.ExpressionVisitor;
import com.nd.gaea.odata.api.uri.queryoption.expression.Literal;

public class LiteralImpl extends ExpressionImpl implements Literal {

  private String text;
  private Class<?> type;

  @Override
  public String getText() {
    return text;
  }

  public LiteralImpl setText(final String text) {
    this.text = text;
    return this;
  }

  @Override
  public Class<?> getType() {
    return type;
  }

  public LiteralImpl setType(final Class<?> type) {
    this.type = type;
    return this;
  }

  @Override
  public <T> T accept(final ExpressionVisitor<T> visitor) throws ExpressionVisitException, ODataApplicationException {
    return visitor.visitLiteral(text);
  }

}

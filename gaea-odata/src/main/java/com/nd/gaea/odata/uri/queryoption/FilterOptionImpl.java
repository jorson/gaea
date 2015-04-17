package com.nd.gaea.odata.uri.queryoption;


import com.nd.gaea.odata.api.uri.queryoption.FilterOption;
import com.nd.gaea.odata.api.uri.queryoption.SystemQueryOptionKind;
import com.nd.gaea.odata.uri.queryoption.expression.ExpressionImpl;

public class FilterOptionImpl extends SystemQueryOptionImpl implements FilterOption {

  private ExpressionImpl expression;

  public FilterOptionImpl() {
    setKind(SystemQueryOptionKind.FILTER);
  }

  public FilterOptionImpl setExpression(final ExpressionImpl expression) {
    this.expression = expression;
    return this;
  }

  @Override
  public ExpressionImpl getExpression() {
    return expression;
  }

}

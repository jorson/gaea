package com.nd.gaea.odata.uri.queryoption;


import com.nd.gaea.odata.api.uri.queryoption.OrderByItem;
import com.nd.gaea.odata.uri.queryoption.expression.ExpressionImpl;

public class OrderByItemImpl implements OrderByItem {

  private ExpressionImpl expression;
  private boolean descending = false; // default sort order is ascending

  @Override
  public boolean isDescending() {
    return descending;
  }

  public OrderByItem setDescending(final boolean descending) {
    this.descending = descending;
    return this;
  }

  @Override
  public ExpressionImpl getExpression() {
    return expression;
  }

  public OrderByItem setExpression(final ExpressionImpl expression) {
    this.expression = expression;
    return this;
  }

}

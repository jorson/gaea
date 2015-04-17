package com.nd.gaea.odata.uri.queryoption.expression;

import com.nd.gaea.odata.api.ODataApplicationException;
import com.nd.gaea.odata.api.uri.queryoption.expression.*;

public class UnaryImpl extends ExpressionImpl implements Unary {

  private UnaryOperatorKind operator;
  private ExpressionImpl expression;

  @Override
  public UnaryOperatorKind getOperator() {
    return operator;
  }

  public void setOperator(final UnaryOperatorKind operator) {
    this.operator = operator;
  }

  @Override
  public Expression getOperand() {
    return expression;
  }

  public void setOperand(final ExpressionImpl expression) {
    this.expression = expression;
  }

  @Override
  public <T> T accept(final ExpressionVisitor<T> visitor) throws ExpressionVisitException, ODataApplicationException {
    T operand = expression.accept(visitor);
    return visitor.visitUnaryOperator(operator, operand);
  }

}

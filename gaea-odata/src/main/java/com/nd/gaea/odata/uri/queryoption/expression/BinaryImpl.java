package com.nd.gaea.odata.uri.queryoption.expression;


import com.nd.gaea.odata.api.ODataApplicationException;
import com.nd.gaea.odata.api.uri.queryoption.expression.*;

public class BinaryImpl extends ExpressionImpl implements Binary {

  private BinaryOperatorKind operator;
  private ExpressionImpl left;
  private ExpressionImpl right;

  @Override
  public BinaryOperatorKind getOperator() {
    return operator;
  }

  public Binary setOperator(final BinaryOperatorKind operator) {
    this.operator = operator;
    return this;
  }

  @Override
  public Expression getLeftOperand() {
    return left;
  }

  public void setLeftOperand(final ExpressionImpl operand) {
    left = operand;
  }

  @Override
  public Expression getRightOperand() {
    return right;
  }

  public void setRightOperand(final ExpressionImpl operand) {
    right = operand;

  }

  @Override
  public <T> T accept(final ExpressionVisitor<T> visitor) throws ExpressionVisitException, ODataApplicationException {
    T left = this.left.accept(visitor);
    T right = this.right.accept(visitor);
    return visitor.visitBinaryOperator(operator, left, right);
  }

}

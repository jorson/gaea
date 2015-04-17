package com.nd.gaea.odata.api.uri.queryoption.expression;

import com.nd.gaea.odata.api.ODataApplicationException;
import com.nd.gaea.odata.api.uri.UriInfoResource;

import java.util.List;

public interface ExpressionVisitor<T> {


    T visitBinaryOperator(BinaryOperatorKind operator, T left, T right)
            throws ExpressionVisitException, ODataApplicationException;

    T visitUnaryOperator(UnaryOperatorKind operator, T operand)
            throws ExpressionVisitException, ODataApplicationException;

    T visitMethodCall(MethodKind methodCall, List<T> parameters)
            throws ExpressionVisitException, ODataApplicationException;

    T visitLambdaExpression(String lambdaFunction, String lambdaVariable, Expression expression)
            throws ExpressionVisitException, ODataApplicationException;

    T visitLiteral(String literal)
            throws ExpressionVisitException, ODataApplicationException;

    T visitMultiLiteral(List<String> literals)
            throws ExpressionVisitException, ODataApplicationException;

    T visitMember(UriInfoResource member)
            throws ExpressionVisitException, ODataApplicationException;

    T visitAlias(String aliasName)
            throws ExpressionVisitException, ODataApplicationException;

    T visitLambdaReference(String variableName)
            throws ExpressionVisitException, ODataApplicationException;


}

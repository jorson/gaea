package com.nd.gaea.odata.api.uri.queryoption.expression;

/**
 * Enumeration of supported unary operators<br>
 * For the semantic of these operators please see the ODATA specification for URL conventions
 */
public enum UnaryOperatorKind {

    /**
     * Minus operator
     */
    MINUS("-"),

    /**
     * not operator
     */
    NOT("not");

    private String syntax;

    /**
     * Constructor for enumeration value
     *
     * @param Syntax used in the URI
     */
    private UnaryOperatorKind(final String syntax) {
        this.syntax = syntax;
    }

    /**
     * @return URI syntax for that operator kind
     */
    @Override
    public String toString() {
        return syntax;
    }

    /**
     * URI syntax to enumeration value
     *
     * @param operator Operator in the syntax used in the URI
     * @return Operator kind which represents the given syntax
     */
    public static UnaryOperatorKind get(final String operator) {
        for (UnaryOperatorKind op : UnaryOperatorKind.values()) {
            if (op.toString().equals(operator)) {
                return op;
            }
        }
        return null;
    }

}

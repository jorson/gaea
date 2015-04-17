package com.nd.gaea.odata.api.uri.queryoption;

/**
 * Defining the supported system query options
 */

public enum SystemQueryOptionKind {

    /**
     * See {@link FilterOption}<br>
     */
    FILTER("$filter"),

    /**
     * See {@link FormatOption}<br>
     */
    FORMAT("$format"),

    /**
     * See {@link ExpandOption}<br>
     */
    EXPAND("$expand"),

    /**
     * See {@link IdOption}<br>
     */
    ID("$id"),

    /**
     * See {@link CountOption}<br>
     */
    COUNT("$count"),

    /**
     * See {@link OrderByOption}<br>
     */
    ORDERBY("$orderby"),

    /**
     * See {@link SearchOption}<br>
     */
    SEARCH("$search"),

    /**
     * See {@link SelectOption}<br>
     */
    SELECT("$select"),

    /**
     * See {@link SkipOption}<br>
     */
    SKIP("$offset"),

    /**
     * See {@link SkipTokenOption}<br>
     */
    SKIPTOKEN("$skiptoken"),

    /**
     * See {@link TopOption}<br>
     */
    TOP("$limit"),

    /**
     * See {@link LevelsExpandOption}<br>
     */
    LEVELS("$level");

    String syntax;

    private SystemQueryOptionKind(final String syntax) {
        this.syntax = syntax;
    }

    @Override
    public String toString() {
        return syntax;
    }
}

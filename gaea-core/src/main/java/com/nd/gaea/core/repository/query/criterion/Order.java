package com.nd.gaea.core.repository.query.criterion;

import java.io.Serializable;

/**
 * 请在这里输入说明
 *
 * @author bifeng.liu
 */
public class Order implements Serializable {
    private boolean ascending;
    private boolean ignoreCase;
    private String propertyName;

    /**
     * Ascending order
     *
     * @param propertyName The property to order on
     * @return The build Order instance
     */
    public static Order asc(String propertyName) {
        return new Order(propertyName, true);
    }

    /**
     * Descending order.
     *
     * @param propertyName The property to order on
     * @return The build Order instance
     */
    public static Order desc(String propertyName) {
        return new Order(propertyName, false);
    }

    /**
     * Constructor for Order.  Order instances are generally created by factory methods.
     *
     * @see #asc
     * @see #desc
     */
    protected Order(String propertyName, boolean ascending) {
        this.propertyName = propertyName;
        this.ascending = ascending;
    }

    /**
     * Should this ordering ignore case?  Has no effect on non-character properties.
     *
     * @return {@code this}, for method chaining
     */
    public Order ignoreCase() {
        ignoreCase = true;
        return this;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public boolean isAscending() {
        return ascending;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (ascending != order.ascending) return false;
        if (ignoreCase != order.ignoreCase) return false;
        if (!propertyName.equals(order.propertyName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (ascending ? 1 : 0);
        result = 31 * result + (ignoreCase ? 1 : 0);
        result = 31 * result + propertyName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Order{ propertyName='" + propertyName + '\'' +
                ", ascending=" + ascending + ", ignoreCase=" + ignoreCase + "}";
    }
}

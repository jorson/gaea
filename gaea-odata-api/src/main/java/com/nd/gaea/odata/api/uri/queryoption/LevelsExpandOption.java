package com.nd.gaea.odata.api.uri.queryoption;

/**
 * Represents the system query option $levels when used inside $expand
 * For example: http://.../Employees?$expand=Model.Manager/DirectReports($levels=3)
 */
public interface LevelsExpandOption {

    /**
     * @return Levels was max
     */
    boolean isMax();

    /**
     * @return Value of $levels
     */
    int getValue();

}

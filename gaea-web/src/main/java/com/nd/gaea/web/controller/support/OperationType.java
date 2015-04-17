package com.nd.gaea.web.controller.support;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller的操作类型
 *
 * @author bifeng.liu
 */
public enum OperationType {
    /**
     * 默认标准访问页面
     */
    PAGE(1),
    /**
     * OPEN API
     */
    OPENAPI(2),
    /**
     * AJAX API
     */
    AJAXAPI(4),
    /**
     * JSONP
     */
    JSONP(8),
    /**
     * OPENAPI OR JSONP
     */
    OPENJSONP(10);

    private int code;

    OperationType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static OperationType[] getEnums(int code) {
        List<OperationType> results = new ArrayList<OperationType>();
        for (OperationType ot : OperationType.values()) {
            if ((code & ot.getCode()) == 1) {
                results.add(ot);
            }
        }
        return results.toArray(new OperationType[]{});
    }
}

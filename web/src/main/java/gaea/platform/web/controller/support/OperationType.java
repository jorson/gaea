package gaea.platform.web.controller.support;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller的操作类型
 *
 * @author bifeng.liu
 */
public enum OperationType {
    PAGE(1),
    OPENAPI(2),
    AJAXAPI(4),
    JSONP(8);

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

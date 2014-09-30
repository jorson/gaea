package gaea.platform.web.controller;

/**
 * OpenApi Controller的返回值
 *
 * @author bifeng.liu
 */
public class OpenApiControllerResult extends ControllerResult {

    /**
     * 数据
     */
    private Object data;

    public OpenApiControllerResult() {
        this(0, "SUCCESS", null);
    }

    public OpenApiControllerResult(int code, String message, Object data) {
        super(code, message);
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

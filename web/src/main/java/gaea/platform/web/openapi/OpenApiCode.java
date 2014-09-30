package gaea.platform.web.openapi;

/**
 * OpenApi 错误返回码
 * @author licong
 * @date 14-9-26
 */
public enum OpenApiCode {

    SUCCESS(0),         // 成功
    PARAM_ERROR(4000),  // 参数错误
    EXCEPTION(50000);   // 异常

    OpenApiCode(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }
}

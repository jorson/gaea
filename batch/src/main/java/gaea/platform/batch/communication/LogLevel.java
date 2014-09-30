package gaea.platform.batch.communication;

/**
 * @author wangchaoxu.
 */
public enum LogLevel {

    DEBUG(1),
    INFO(2),
    WARN(3),
    ERROR(4),
    FATAL(5);

    private int value;

    LogLevel(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

}

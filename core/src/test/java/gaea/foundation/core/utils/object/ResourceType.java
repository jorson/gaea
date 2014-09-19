package gaea.foundation.core.utils.object;

/**
 * @author wangchaoxu.
 */
public enum ResourceType {

    /// 视频
    VIDEO(0),
    /// 文档
    DOCUMENT(1),
    ///// 试卷
    PAPER(2),
    /// 练习卷
    UNITWORD(9);

    private int value;

    ResourceType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ResourceType getResourceType(int value) {
        switch (value) {
            case 0:
                return VIDEO;
            case 1:
                return DOCUMENT;
            case 2:
                return PAPER;
            case 9:
                return UNITWORD;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

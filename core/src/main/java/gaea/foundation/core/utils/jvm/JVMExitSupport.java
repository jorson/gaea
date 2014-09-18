package gaea.foundation.core.utils.jvm;

/**
 * Java虚拟机退出支持类
 *
 * @author wuhy
 */
public class JVMExitSupport {

    /**
     * 退出Java程序
     *
     * @param status 状态码，非0的状态码表示异常终止
     */
    public static void exit(int status) {
        System.exit(status);
    }

    /**
     * 强行退出Java程序
     * <p/>
     * 该方法会直接退出，不管程序中是否还有线程在处理
     *
     * @param status 状态码，非0的状态码表示异常终止
     */
    public static void halt(int status) {
        Runtime.getRuntime().halt(status);
    }
}

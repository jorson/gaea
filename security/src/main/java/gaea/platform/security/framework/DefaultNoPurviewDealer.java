package gaea.platform.security.framework;

import gaea.platform.security.access.User;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 默认的鉴权失败处理器
 * Created by wuhy on 14-6-4.
 */
public class DefaultNoPurviewDealer implements NoPurviewDealer {

    @Override
    public Object deal(ProceedingJoinPoint joinPoint, User user, Security security) {
        if (isController(joinPoint)) {
            return "/errors/nopurview";
        }
        return null;
    }

    /**
     * 是否Controller
     * @param joinPoint
     * @return true:是
     */
    private boolean isController(ProceedingJoinPoint joinPoint) {
        return -1 != joinPoint.getTarget().getClass().getCanonicalName().indexOf("Controller");
    }
}

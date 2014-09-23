package gaea.platform.security.framework;

import gaea.platform.security.access.User;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * AOP鉴权失败处理器
 * Created by wuhy on 14-6-4.
 */
public interface NoPurviewDealer {

    /**
     * 实现处理失败的逻辑
     * @param joinPoint     切点
     * @param user          用户信息
     * @param security      切点声明的权限
     * @return
     */
    public Object deal(ProceedingJoinPoint joinPoint, User user, Security security);
}

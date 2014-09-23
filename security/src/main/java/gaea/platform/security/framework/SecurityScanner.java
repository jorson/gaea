package gaea.platform.security.framework;

import gaea.foundation.core.utils.CollectionUtils;
import gaea.platform.security.access.User;
import gaea.platform.security.context.UserContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import static gaea.platform.security.framework.PurviewJudger.*;

/**
 * 权限AOP
 * User: wuhy
 * Date: 14-5-30
 * Time: 上午11:43
 */
public class SecurityScanner {

    private NoPurviewDealer noPurviewDealer;

    /**
     * 获取方法声明的权限定义，判断用户是否有权限执行该方法
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取当前用户
        User user = UserContext.getCurrentUser();

        // 没有用户直接放过，因为Filter尚未写入用户
        if (null == user) {
            return joinPoint.proceed();
        }

        //获取方法注解
        Signature signature = joinPoint.getSignature();
        Security security = ((MethodSignature) signature).getMethod().getAnnotation(Security.class);

        if (null == security) {
            //获取类注解
            security = joinPoint.getTarget().getClass().getAnnotation(Security.class);
        }

        //方法与类都没有Security注解，无需鉴权
        if (null == security) {
            return joinPoint.proceed();
        }

        //鉴权
        if (hasPurview(security.value(), user)) {
            return joinPoint.proceed();
        }
        return noPurviewDealer.deal(joinPoint, user, security);
    }

    public void setNoPurviewDealer(NoPurviewDealer noPurviewDealer) {
        this.noPurviewDealer = noPurviewDealer;
    }
}

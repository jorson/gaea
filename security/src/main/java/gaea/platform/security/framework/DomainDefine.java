package gaea.platform.security.framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 域定义注解
 * 用于对域进行定义，只能定义在类上，域的注册需要类集成DomainRegister实现
 * @see com.huayu.foundation.security.framework.DomainRegister
 * @author wuhy
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainDefine {

    /**
     * 域的KEY值
     * @return
     */
    public String key();

    /**
     * 域名称
     * @return
     */
    public String name();

}

package gaea.platform.security.framework;

import gaea.foundation.core.utils.StringUtils;
import gaea.platform.security.domain.Domain;
import gaea.platform.security.domain.Purview;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 域注册器
 * 系统包含的所有域通过集成该抽象类并声明Spring注入实现注册
 * Spring注入声明
 * @see org.springframework.stereotype.Component
 * 域定义使用注解@Domain来实现
 * @see DomainDefine
 * Created by wuhy on 14-6-6.
 */
public abstract class DomainRegister {

    private static final Log LOG = LogFactory.getLog(DomainRegister.class);


    public DomainRegister() {
        regist();
    }

    /**
     * 注册
     */
    private void regist() {

        // 获取类对象
        Class clazz = this.getClass();

        /**
         *  1、扫描@Domain注解，构造Domain实例
         */
        DomainDefine domainDefine = (DomainDefine) clazz.getAnnotation(DomainDefine.class);
        if (null == domainDefine) {
            LOG.error("域定义错误，未使用@Domain声明域定义，Class=" + this.getClass().getCanonicalName());
            return;
        }
        String domainKey = domainDefine.key();
        if(StringUtils.isEmpty(domainKey)) {
            LOG.error("域定义错误，@Domain声明的key为空，Class=" + this.getClass().getCanonicalName());
            return;
        }
        String domainName = domainDefine.name();
        if(StringUtils.isEmpty(domainKey)) {
            LOG.error("域定义错误，@Domain声明的name为空，Class=" + this.getClass().getCanonicalName());
            return;
        }
        Domain domain = new Domain();
        domain.setName(domainName);
        domain.setId(domainKey);

        // 交由DomainManager管理
        DomainManager.add(domain);

        /**
         *   2、扫描@Purview注解，构造Purview实例
         */
        Field[] fields = clazz.getDeclaredFields();

        // Map<权限KEY，权限>
        Map<String, Purview> map = new HashMap<String, Purview>();

        // List<父权限KEY>
        List<String> parents = new ArrayList<String>();

        // 遍历所有字段，把@PurviewDefine注解的字段还原为Purview
        Purview purview;
        String purviewId = null;
        PurviewDefine define = null;
        for (Field field : fields) {
            if (isDeclarePurviewField(field)) {
                purviewId = getId(field);
                if (null != purviewId) {
                    define = field.getAnnotation(PurviewDefine.class);
                    purview = new Purview();
                    purview.setId(purviewId);
                    purview.setName(define.value());
                    purview.setDomainKey(domainKey);
                    if (!StringUtils.isEmpty(define.parent())) {
                        purview.setParent(define.parent());
                        parents.add(define.parent());
                    }
                    map.put(purviewId, purview);
                }
            }
        }

        // 处理父权限，设置isParent为true
        Iterator<Map.Entry<String, Purview>> iterator = map.entrySet().iterator();
        Map.Entry<String, Purview> entry = null;
        while (iterator.hasNext()) {
            entry = iterator.next();
            entry.getValue().setIsParent(parents.contains(entry.getKey()));
            // 交由PurviewManger管理
            PurviewManger.add(entry.getValue());
        }
    }

    /**
     * 是否是声明了权限的字段
     *
     * @param field  字段
     * @return  true:是
     */
    private boolean isDeclarePurviewField(Field field) {
        return field.isAnnotationPresent(PurviewDefine.class) && isPublicStaticFinalString(field);
    }

    /**
     * 字段是否为 public static final String
     *
     * @param field 字段
     * @return  true:是
     */
    private boolean isPublicStaticFinalString(Field field) {
        return Modifier.isStatic(field.getModifiers()) &&  Modifier.isFinal(field.getModifiers()) && field.getType() == String.class;
    }

    /**
     * 获取ID
     * @param field 字段
     * @return  ID
     */
    private String getId(Field field) {
        try {
            return ((String) field.get(null));
        } catch (IllegalAccessException e) {
            LOG.error("获取@Purview声明的字段的值时出错,class=" + field.getDeclaringClass().getCanonicalName()
                    + ",field=" + field.getName());
            return null;
        }
    }
}

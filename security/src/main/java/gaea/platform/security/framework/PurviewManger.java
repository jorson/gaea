package gaea.platform.security.framework;

import gaea.foundation.core.utils.CollectionUtils;
import gaea.platform.security.context.UserContext;
import gaea.platform.security.domain.Purview;
import gaea.platform.security.service.PurviewService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 权限管理工具，用于管理系统内所有权限
 * Created by wuhy on 14-6-6.
 */
public class PurviewManger {

    private static Set<Purview> PURVIEW_SET = new HashSet<Purview>();

    /**
     * 添加权限
     * @param purview
     */
    public static final void add(Purview purview) {
        PURVIEW_SET.add(purview);
    }

    /**
     * 获取所有权限
     * @return
     */
    public synchronized static final List<Purview> getAll() {
        if(CollectionUtils.isEmpty(PURVIEW_SET)) {
            //从数据库获取权限集合
            WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext((
                    (HttpServletRequest) UserContext.getRequest()).getSession().getServletContext());
            PurviewService purviewService =
                    (PurviewService)context.getBean("purviewService");
            PURVIEW_SET.addAll(purviewService.findAll());
        }
        return CollectionUtils.setAsList(PURVIEW_SET);
    }
}

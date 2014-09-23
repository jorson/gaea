package gaea.platform.security.service;

import gaea.foundation.core.service.EntityService;
import gaea.platform.security.domain.Purview;

import java.util.List;

/**
 * User: wuhy
 * Date: 14-5-30
 * Time: 下午6:15
 */
public interface PurviewService extends EntityService<Purview> {

    /**
     * 查看当前项目所有权限
     * @return
     */
    public List<Purview> findAll();
}

package com.nd.gaea.odata;

import com.nd.gaea.core.repository.query.QuerySupport;
import com.nd.gaea.core.service.AbstractService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2014-11-24.
 */
public class ODataHandler {

    /**
     * 处理OData的查询,获取指定对象的数据列表
     * @param <T> 对象类型
     * @return 数据列表
     */
    public static <T> Object process(final HttpServletRequest request, AbstractService<T> entityService,
                                      Class<?> filterClass){
        if(request == null || entityService == null) {
            throw new IllegalArgumentException("arguments request or entityService is null");
        }

        QuerySupport support = ODataQueryHandler.getInstance().process(request, filterClass);
        //如果查询条件中包含$Count
        if(support.getHasCount()) {
            //如果不包含有Skip或Limit, 说明只是进行Count操作. 否则就是分页操作
            if(support.getLimit() == 0 && support.getOffset() == 0) {
                return entityService.count(support);
            } else {
                return entityService.findPager(support);
            }
        }
        //如果查询条件中没有包含$Count
        return entityService.find(support);
    }
}

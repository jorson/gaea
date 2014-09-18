package gaea.foundation.core.service;

import gaea.foundation.core.bo.EntityObject;
import gaea.foundation.core.repository.query.QuerySupport;
import gaea.foundation.core.repository.query.criterion.Criterion;
import gaea.foundation.core.repository.support.Pager;

public interface EntityPagedQueryable<T extends EntityObject> {
    /**
     * 根据查询对象分页数据，会返回totalCount
     *
     * @param querySupport 查询对象
     * @return 含总记录数和当前页数据的Page对象.
     */
    public Pager pagedQuery(QuerySupport querySupport);

    /**
     * 根据开始页数、每页记录数、排序、条件取得分页数据，会返回totalCount
     * <p/>
     * 当totalCount传入小于0的数时，才会重新计算totalCount
     *
     * @param pageNo     开始页数，从1开始
     * @param pageSize   每页记录数
     * @param criterions 条件
     * @return 含总记录数和当前页数据的Page对象.
     */
    public Pager pagedQuery(int pageNo, int pageSize, Criterion... criterions);

    /**
     * 根据开始页数、每页记录数、条件取得分页数据，会返回totalCount
     * <p/>
     * 当totalCount传入小于0的数时，才会重新计算totalCount
     *
     * @param pageNo     开始页数，从1开始
     * @param pageSize   每页记录数
     * @param totalCount 总页面数
     * @param orderBy    排序
     * @param criterions 条件
     * @return 含总记录数和当前页数据的Page对象.
     */
    public Pager pagedQuery(int pageNo, int pageSize, long totalCount, T queryObject);

    /**
     * 根据开始页数、每页记录数、排序、条件取得分页数据，会返回totalCount
     * <p/>
     * 当totalCount传入小于0的数时，才会重新计算totalCount
     *
     * @param pageNo     开始页数，从1开始
     * @param pageSize   每页记录数
     * @param totalCount 总页面数
     * @param orderBy    排序
     * @param criterions 条件
     * @return 含总记录数和当前页数据的Page对象.
     */
    public Pager pagedQuery(int pageNo, int pageSize, long totalCount, String orderBy, Criterion... criterions);

    /**
     * 根据查询对象分页数据，会返回totalCount
     *
     * @param querySupport 查询对象
     * @return 含总记录数和当前页数据的Page对象.
     */
    public Pager pagedQuery(T queryObject, Pager pager);
}

package gaea.foundation.core.repository.extend;

//import org.hibernate4.criterion.Criterion;

import gaea.foundation.core.repository.query.QuerySupport;

import java.util.List;

/**
 * 定义如果支持Entity不被直接删除必须支持的Operation.
 *
 * @author wuhy
 */
public interface UndeletableEntityOperation<T> {

    /**
     * 查询表或者集合中所有标志有效的数据
     *
     * @return
     */
    List<T> findAllValid();

    /**
     * 根据查询条件取得表或者集合中满足条件的所有标志有效的数据
     *
     * @param queryData 栏位与值列表
     * @return
     */
    List<T> findValid(QuerySupport querySupport);

    /**
     * 根据查询对象取得表或者集合中满足条件的所有标志有效的数据
     *
     * @param example
     * @return
     */
    List<T> findValid(T queryObject);

    /**
     * 删除对象
     * <p/>
     * 如果是Undeleteable的entity,设置对象的状态而不是直接删除.
     *
     * @param removeObject
     */
    void delete(Object removeObject);
}

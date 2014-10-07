package gaea.foundation.core.repository.shard;

import gaea.foundation.core.repository.support.Pager;

import java.util.Collection;
import java.util.List;

/**
 * Created by Jorson on 14-10-4.
 */
public interface SimpleSession<TEntity> {

    void create(TEntity entity);

    void update(TEntity entity);

    void delete(TEntity entity);

    TEntity get(Object id);

    List<TEntity> getList(Collection ids);

    TEntity findOne()
}

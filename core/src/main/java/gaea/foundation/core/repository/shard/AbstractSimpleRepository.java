package gaea.foundation.core.repository.shard;

import gaea.foundation.core.repository.SimpleRepository;
import gaea.foundation.core.specifications.SimpleSpecification;

import java.util.Collection;

/**
 * 支持简单仓储的抽象类
 */
public abstract class AbstractSimpleRepository<TEntity> implements SimpleRepository<TEntity> {

    public AbstractSimpleRepository() {

    }

    @Override
    public TEntity get(Object id) {
        return null;
    }

    @Override
    public Collection<TEntity> getList(Collection<TEntity> ids) {
        return null;
    }

    @Override
    public void create(TEntity tEntity) {

    }

    @Override
    public void update(TEntity tEntity) {

    }

    @Override
    public void delete(Object id) {

    }

    @Override
    public TEntity findOne(SimpleSpecification specification) {
        return null;
    }

    @Override
    public Collection<TEntity> findAll(SimpleSpecification specification) {
        return null;
    }
}

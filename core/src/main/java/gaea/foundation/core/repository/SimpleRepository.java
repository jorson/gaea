package gaea.foundation.core.repository;

import gaea.foundation.core.specifications.SimpleSpecification;

import java.util.Collection;

/**
 * 实现简单功能的Repository
 */
public interface SimpleRepository<TEntry> {

    TEntry get(Object id);

    Collection<TEntry> getList(Collection<TEntry> ids);

    void create(TEntry entry);

    void update(TEntry entry);

    void delete(Object id);

    TEntry findOne(SimpleSpecification specification);

    Collection<TEntry> findAll(SimpleSpecification specification);
}

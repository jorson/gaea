package com.nd.gaea.odata.api.processor;

/**
 * Created by Administrator on 2014-11-20.
 */
public interface QueryProcessor<T> {

    public T process(QueryOptions options, Class<?> filterClass);
}

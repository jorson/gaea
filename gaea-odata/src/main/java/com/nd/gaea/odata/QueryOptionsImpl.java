package com.nd.gaea.odata;

import com.nd.gaea.odata.api.processor.QueryOptions;
import com.nd.gaea.odata.api.uri.UriInfoKind;
import com.nd.gaea.odata.api.uri.queryoption.SystemQueryOption;
import com.nd.gaea.odata.api.uri.queryoption.SystemQueryOptionKind;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2014-11-20.
 */
public final class QueryOptionsImpl implements QueryOptions {

    private final Map<SystemQueryOptionKind, SystemQueryOption> optionMap;
    private UriInfoKind queryKind = UriInfoKind.service;

    public QueryOptionsImpl() {
        optionMap = new HashMap<>();
    }

    public QueryOptionsImpl(UriInfoKind kind) {
        this();
        this.queryKind = kind;
    }

    @Override
    public UriInfoKind getQueryKind() {
        return this.queryKind;
    }

    @Override
    public <T extends SystemQueryOption> T getQueryOption(SystemQueryOptionKind kind) {
        SystemQueryOption option = optionMap.get(kind);
        if(option == null) {
            return null;
        }
        return (T)option;
    }

    @Override
    public boolean containQueryOption(SystemQueryOptionKind kind) {
        return this.optionMap.containsKey(kind);
    }

    /**
     * 设置QueryOption
     * @param option 查询
     * @param <T>
     */
    @Override
    public <T extends SystemQueryOption> void setQueryOption(T option) {
        //如果传入对象为空, 直接返回并忽略
        if(option == null) {
            return;
        }
        //写入集合
        if(!optionMap.containsKey(option.getKind())) {
            optionMap.put(option.getKind(), option);
        }
    }
}

package com.nd.gaea.web.model.mapper;

import com.nd.gaea.core.model.ViewModel;
import com.nd.gaea.core.model.converter.AbstractModelConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014-11-24.
 */
public class AutoModelMapper {

    protected final static Logger LOGGER = LoggerFactory.getLogger(AutoModelMapper.class);

    private Map<Class<? extends ViewModel>, AbstractModelConverter> targetConverterMapSet;

    private static AutoModelMapper instance;
    private static Object syncRoot = new Object();

    public static AutoModelMapper getInstance() {
        if(instance == null) {
            synchronized (syncRoot) {
                if(instance == null) {
                    instance = new AutoModelMapper();
                }
            }
        }
        return instance;
    }

    protected AutoModelMapper() {
        targetConverterMapSet= new HashMap<>();
    }

    /**
     * 注册目标对象和目标对象对应的转换器对象
     * @param viewModel 目标对象类型
     * @param converter 转换器类型
     */
    public void register(Class<? extends ViewModel> viewModel, Class<? extends AbstractModelConverter> converter) {
        try {
            if(!targetConverterMapSet.containsKey(viewModel)) {
                if(converter.equals(AbstractModelConverter.class)) {
                    targetConverterMapSet.put(viewModel, null);
                } else {
                    targetConverterMapSet.put(viewModel, converter.newInstance());
                }
            }
        } catch (Exception e1) {
            if(LOGGER.isErrorEnabled()) {
                LOGGER.error("register model converter error", e1);
            }
        }
    }

    /**
     * 将原对象处理为目标对象
     * @param source 原对象实体
     * @param targetClz 目标对象类
     * @return 目标对象实体
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public Object process(Object source, Class<? extends ViewModel> targetClz)
            throws IllegalAccessException, InstantiationException {
        Class<?> sourceCls = source.getClass();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        Object result = targetClz.newInstance();

        //搜索对应的Convert
        if(targetConverterMapSet.containsKey(targetClz)) {
            AbstractModelConverter convertClz = targetConverterMapSet.get(targetClz);
            if(convertClz != null) {
                mapper.addConverter(convertClz);
            }
        }
        //如果是派生自List类型
        if(List.class.isAssignableFrom(sourceCls)) {
            Type type = sourceCls.getGenericSuperclass();
            //如果数据源是一个泛型对象对象
            if(type instanceof ParameterizedType) {
                Type genericType = targetClz.newInstance().genListViewModelType();
                result = mapper.map(source, genericType);
            }
        }
        //如果派生自Map类型
        else if(Map.class.isAssignableFrom(sourceCls)) {
            throw new UnsupportedOperationException("source class can't be " + sourceCls.getSimpleName());
        }
        //其他都认为是派生Object类型
        else {
            result = mapper.map(source, targetClz);
        }
        return result;
    }

    public int getMapperCount() {
        return targetConverterMapSet.size();
    }
}

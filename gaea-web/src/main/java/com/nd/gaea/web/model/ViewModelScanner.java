package com.nd.gaea.web.model;

import com.nd.gaea.core.model.ViewModel;
import com.nd.gaea.core.model.ViewModelMapping;
import com.nd.gaea.utils.StringUtils;
import com.nd.gaea.web.model.mapper.AutoModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2014-11-25.
 */
public class ViewModelScanner {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ViewModelScanner.class);

    private static final String RESOURCE_PATTERN = "/**/*.class";
    private ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    private List<String> packagesList = new LinkedList<String>();
    private List<TypeFilter> typeFilters = new LinkedList<TypeFilter>();

    public ViewModelScanner(String[] packagePaths, Class<? extends ViewModelMapping>... filter) {
        //确认需要扫描的包路径, 支持多个包路径的扫描
        if(packagePaths == null && packagePaths.length == 0) {
            throw new IllegalArgumentException("package paths can't NULL or empty");
        } else {
            for(String path : packagePaths) {
                this.packagesList.add(path);
            }
        }
        //如果输入的Filter参数为空, 默认加上ViewModelMapping的筛选
        if(filter == null) {
            typeFilters.add(new AnnotationTypeFilter(ViewModelMapping.class, false));
        } else {
            for(Class<? extends ViewModelMapping> annotation : filter) {
                typeFilters.add(new AnnotationTypeFilter(annotation, false));
            }
        }
    }

    public void scanPackage() throws IOException, ClassNotFoundException {
        for(String path : this.packagesList) {
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    ClassUtils.convertClassNameToResourcePath(path) + RESOURCE_PATTERN;
            Resource[] resources = this.resolver.getResources(pattern);
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(this.resolver);

            for(Resource res : resources) {
                //如果资源可读
                if(res.isReadable()) {
                    MetadataReader reader = readerFactory.getMetadataReader(res);
                    //如果可以匹配
                    if(matches(reader, readerFactory)) {
                        registerViewModelMapping(reader.getClassMetadata().getClassName());
                    }
                }
            }
        }
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("load view model class over!");
        }
    }

    private void registerViewModelMapping(String className) throws ClassNotFoundException {
        if(StringUtils.isNotEmpty(className)) {
            boolean implViewMode = false;
            Class<?> clz = Class.forName(className);
            //判断CLZ是否实现ViewModel接口
            for(Class<?> cls : clz.getInterfaces()) {
                if(cls.isAssignableFrom(ViewModel.class)) {
                    implViewMode = true;
                    break;
                }
            }
            if(implViewMode) {
                Annotation[] annotations =  clz.getAnnotations();
                //循环所有的注解, 但是只获取第一个继承VieModelMapping的注解
                for (Annotation annotation : annotations) {
                    if(annotation instanceof ViewModelMapping) {
                        ViewModelMapping mapping = (ViewModelMapping)annotation;
                        AutoModelMapper.getInstance().register(clz.asSubclass(ViewModel.class), mapping.converter());
                    }
                }
            }
        }
    }

    /**
     * 检查当前扫描的类是否存在指定Filter的注解
     */
    private boolean matches(MetadataReader reader, MetadataReaderFactory readerFactory)
            throws IOException {
        if(!this.typeFilters.isEmpty()) {
            for(TypeFilter filter : this.typeFilters) {
                if(filter.match(reader, readerFactory)) {
                    return true;
                }
            }
        }
        return false;
    }
}

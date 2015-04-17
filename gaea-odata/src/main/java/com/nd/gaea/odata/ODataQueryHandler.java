package com.nd.gaea.odata;

import com.nd.gaea.odata.api.ODataRuntimeException;
import com.nd.gaea.odata.api.processor.ODataQuerySupport;
import com.nd.gaea.odata.api.processor.QueryOptions;
import com.nd.gaea.odata.api.processor.QueryProcessor;
import com.nd.gaea.odata.api.uri.UriInfo;
import com.nd.gaea.odata.processor.DefaultQueryProcessor;
import com.nd.gaea.odata.uri.parser.Parser;
import com.nd.gaea.odata.uri.parser.UriParserException;
import com.nd.gaea.core.repository.query.QuerySupport;
import com.nd.gaea.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 提供进行OData查询转换的处理器
 * Created by Administrator on 2014-11-20.
 */
public class ODataQueryHandler {

    private final static Logger LOG = LoggerFactory.getLogger(ODataQueryHandler.class);
    private final Map<Class<? extends QueryProcessor>, QueryProcessor> processors = new HashMap<>();

    private static ODataQueryHandler instance;
    private static Object syncRoot = new Object();

    public static ODataQueryHandler getInstance() {
        if(instance == null) {
            synchronized (syncRoot) {
                if(instance == null) {
                    instance = new ODataQueryHandler();
                }
            }
        }
        return instance;
    }

    protected ODataQueryHandler() {
        //注册默认的查询处理器
        register(new DefaultQueryProcessor());
    }

    /**
     * 将oData转换为QuerySupport对象
     * @return
     */
    public QuerySupport process(final HttpServletRequest request, final Class<?> filterClass) {
        try {
            //从Request中获取数据的解析
            QueryOptions options = getODataOptionFromRequest(request);
            if(options == null) {
                throw new NullPointerException("can not found any option in HTTPRequest");
            }

            switch (options.getQueryKind()) {
                case service:
                    DefaultQueryProcessor processor = selectProcessor(DefaultQueryProcessor.class);
                    ODataQuerySupport support = processor.process(options, filterClass);
                    //如果处理过程中存在异常
                    if(support.getParseExceptions().size() > 0) {
                        StringBuilder builder = new StringBuilder();
                        for(Exception ex : support.getParseExceptions()) {
                            builder.append(ex.getMessage() + "\n");
                        }
                        throw new ODataRuntimeException(builder.toString());
                    }
                    //如果没有异常
                    return support.getQuerySupport();
                default:
                    throw new UnsupportedOperationException("not implemented");
            }

        } catch (ODataQueryHandleException e1) {
            if(LOG.isErrorEnabled()) {
                LOG.error("odata query error!", e1);
            }
        } catch (UriParserException e2) {
            if(LOG.isErrorEnabled()) {
                LOG.error("odata query error!", e2);
            }
        }
        return null;
    }

    /**
     * 注册查询处理器
     * @param processor 查询处理器对象
     */
    public void register(final QueryProcessor processor) {

        for(Class<?> cls : processor.getClass().getInterfaces()) {
            if(QueryProcessor.class.isAssignableFrom(cls)) {
                @SuppressWarnings("unchecked")
                Class<? extends QueryProcessor> procClass = processor.getClass();
                processors.put(procClass, processor);
            }
        }
    }

    private <T extends QueryProcessor> T selectProcessor(final Class<T> cls) {
        @SuppressWarnings("unchecked")
        T processor = (T)processors.get(cls);
        if(processor == null) {
            throw new IllegalArgumentException("Processor:" + cls.getName() + " not register");
        }
        return processor;
    }

    private QueryOptions getODataOptionFromRequest(HttpServletRequest request)
            throws ODataQueryHandleException, UriParserException {
        String method = request.getMethod();
        //目前只接受HTTPGet的请求
        if(!method.equalsIgnoreCase("get")) {
            throw new ODataQueryHandleException("support http get method only!",
                    ODataQueryHandleException.MessageKeys.HTTP_METHOD_NOT_IMPLEMENTED, null);
        }
        //在QueryString前补上'?'
        String dataValue = request.getQueryString();
        //如果查询字符串为空
        if(StringUtils.isEmpty(dataValue)) {
            return null;
        }

        if(!dataValue.startsWith("?")) {
            dataValue = "?" + dataValue;
        }
        //对OData数据进行转换, 如果转换错误会以异常的方式抛出
        UriInfo uriInfo = new Parser().parseUri(dataValue);
        QueryOptions entry = new QueryOptionsImpl(uriInfo.getKind());
        entry.setQueryOption(uriInfo.getIdOption());
        entry.setQueryOption(uriInfo.getFilterOption());
        entry.setQueryOption(uriInfo.getTopOption());
        entry.setQueryOption(uriInfo.getSkipOption());
        entry.setQueryOption(uriInfo.getCountOption());
        entry.setQueryOption(uriInfo.getOrderByOption());
        return entry;
    }
}

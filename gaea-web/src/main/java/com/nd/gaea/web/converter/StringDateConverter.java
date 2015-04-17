package com.nd.gaea.web.converter;

import com.nd.gaea.utils.DateUtils;
import com.nd.gaea.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * Spring MVC转换器，把String转换成Date类型
 * <p/>
 * 在提交时，如果注入的接收方，类型为Date，
 * 则默认把客户端提交的文本转换日期
 *
 * @author bifeng.liu
 * @see DateStringConverter
 * @since 2014-05-08
 */
public class StringDateConverter implements Converter<String, Date> {
    /**
     * LOGGER对象
     */
    private static Log logger = LogFactory.getLog(StringDateConverter.class);

    public final static String DEFAULT_FORMAT = "yyyy-MM-dd";

    @Override
    public Date convert(String source) {
        try {
            return StringUtils.isEmpty(source) ? null : DateUtils.parse(source, DEFAULT_FORMAT);
        } catch (IllegalArgumentException ex) {
            logger.warn("日期转换出错", ex);
            return null;
        }
    }
}

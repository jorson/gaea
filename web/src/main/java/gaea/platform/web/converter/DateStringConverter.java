package gaea.platform.web.converter;

import gaea.foundation.core.utils.DateUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * Spring MVC转换器，把Date格式化相应格式的字符串
 * <p/>
 * 在展现时，如果对象类型为Date，
 * 则默认把日期转换成相应格式的字符串进行显示。
 *
 * @author bifeng.liu
 * @see StringDateConverter
 * @since 2013-05-08
 */
public class DateStringConverter implements Converter<Date, String> {
    public final static String DEFAULT_FORMAT = "yyyy-MM-dd";

    @Override
    public String convert(Date source) {
        return source != null ? DateUtils.format(source, DEFAULT_FORMAT) : null;
    }
}

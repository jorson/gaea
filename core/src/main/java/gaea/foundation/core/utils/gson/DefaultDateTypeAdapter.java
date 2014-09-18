package gaea.foundation.core.utils.gson;

import com.google.gson.*;
import gaea.foundation.core.utils.StringUtils;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.*;

/**
 * 日期类型适配器
 *
 * @author wuhy
 */
public class DefaultDateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

    protected static final String[] DEFAULT_ACCEPTABLE_FORMATS;
    protected static final String DEFAULT_PATTERN;
    protected static final TimeZone DEFAULT_TIMEZONE;

    static {
        DEFAULT_TIMEZONE = TimeZone.getTimeZone("GMT+8");

        DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
        final List acceptablePatterns = new ArrayList();

        acceptablePatterns.add("yyyy-MM-dd HH:mm:ss");
        acceptablePatterns.add("yyyy-MM-dd");
        acceptablePatterns.add("yyyy-MM-dd HH:mm:ss.S");
        DEFAULT_ACCEPTABLE_FORMATS = (String[]) acceptablePatterns.toArray(new String[acceptablePatterns.size()]);
    }

    private final ThreadSafeSimpleDateFormat defaultFormat;
    private final ThreadSafeSimpleDateFormat[] acceptableFormats;
    private final Locale locale;


    /**
     * Construct a DateTypeAdapter with standard formats and lenient set off.
     */
    public DefaultDateTypeAdapter() {
        this(DEFAULT_PATTERN, DEFAULT_ACCEPTABLE_FORMATS, Locale.CHINESE, DEFAULT_TIMEZONE, false);
    }

    /**
     * Construct a DateTypeAdapter with standard formats and lenient set off.
     */
    public DefaultDateTypeAdapter(String defaultFormat) {
        this(defaultFormat, DEFAULT_ACCEPTABLE_FORMATS, Locale.CHINESE, DEFAULT_TIMEZONE, false);
    }

    /**
     * Construct a DateTypeAdapter.
     *
     * @param defaultFormat     the default format
     * @param acceptableFormats fallback formats
     * @param locale            locale to use for the format
     * @param timeZone          the TimeZone used to serialize the Date
     * @param lenient           the lenient setting of {@link java.text.SimpleDateFormat#setLenient(boolean)}
     * @since 1.4.4
     */
    public DefaultDateTypeAdapter(String defaultFormat, String[] acceptableFormats,
                                  Locale locale, TimeZone timeZone, boolean lenient) {
        this.locale = locale;
        this.defaultFormat = new ThreadSafeSimpleDateFormat(defaultFormat, timeZone, locale, 4, 20, lenient);
        this.acceptableFormats = acceptableFormats != null
                ? new ThreadSafeSimpleDateFormat[acceptableFormats.length]
                : new ThreadSafeSimpleDateFormat[0];
        for (int i = 0; i < this.acceptableFormats.length; i++) {
            this.acceptableFormats[i] = new ThreadSafeSimpleDateFormat(acceptableFormats[i], timeZone, 1, 20, lenient);
        }
    }


    /**
     * JSON字符串转为对象时调用
     *
     * @param json
     * @param typeOfT
     * @param context
     * @return
     * @throws com.google.gson.JsonParseException
     */
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!(json instanceof JsonPrimitive)) {
            throw new JsonParseException("The date should be a string value");
        }
        String str = json.getAsString();
        if(StringUtils.isEmpty(str)){
            return null;
        }
        try {
            return (Date)(((Class) typeOfT).getConstructor(Long.TYPE).newInstance(defaultFormat.parse(str).getTime()));
        } catch (Exception e) {
            // try next ...
        }
        for (int i = 0; i < acceptableFormats.length; i++) {
            try {
                return acceptableFormats[i].parse(str);
            } catch (ParseException e3) {
                // no worries, let's try the next format.
            }
        }
        // no dateFormats left to try
        throw new JsonParseException("Cannot parse date " + str);

    }

    /**
     * 对象转为JSON字符串时调用
     *
     * @param src
     * @param typeOfSrc
     * @param context
     * @return
     */
    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        String dateFormatAsString = defaultFormat.format(src);//format.format(new Date(src.getTime()));
        return new JsonPrimitive(dateFormatAsString);
    }
}

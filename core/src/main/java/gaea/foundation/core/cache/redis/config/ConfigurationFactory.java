package gaea.foundation.core.cache.redis.config;

import gaea.foundation.core.cache.support.CacheException;
import gaea.foundation.core.utils.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Redis配置信息读取工厂类
 *
 * @author wuhy
 */
public final class ConfigurationFactory {

    /**
     * Logger对象
     */
    private static final Log logger = LogFactory.getLog(ConfigurationFactory.class);

    private static final String DEFAULT_CLASSPATH_CONFIGURATION_FILE = "/redis.xml";



    /**
     * Constructor.
     */
    private ConfigurationFactory() {

    }

    /**
     * Configures a bean from an XML file available as an URL.
     */
    public static Configuration parseConfiguration(final URL url) throws CacheException {
        logger.debug("Configuring redis from URL: {" + url + "}");
        Configuration configuration;
        InputStream input = null;
        try {
            input = url.openStream();
            configuration = parseConfiguration(input);
        } catch (Exception e) {
            throw new net.sf.ehcache.CacheException("Error configuring from " + url + ". Initial cause was " + e.getMessage(), e);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                logger.error("IOException while closing configuration input stream. Error was " + e.getMessage());
            }
        }
        //configuration.setSource(ConfigurationSource.getConfigurationSource(url));
        return configuration;
    }

    /**
     * Configures a bean from an XML input stream.
     */
    public static Configuration parseConfiguration(final InputStream inputStream) throws net.sf.ehcache.CacheException {

        logger.debug("Configuring redis from InputStream");
        Configuration configuration = new Configuration();
        try {
            String translatedString = translateSystemProperties(inputStream);
            configuration.parseRedisConfiguration(translatedString);
        } catch (Exception e) {
            throw new net.sf.ehcache.CacheException("Error configuring from input stream. Initial cause was " + e.getMessage(), e);
        }
        //configuration.setSource(ConfigurationSource.getConfigurationSource(inputStream));
        return configuration;
    }

    /**
     * 从默认的XML中转换成对象
     */
    public static Configuration parseConfiguration() throws CacheException {
        ClassLoader standardClassloader = ClassUtils.getStandardClassLoader();
        URL url = null;
        if (standardClassloader != null) {
            url = standardClassloader.getResource(DEFAULT_CLASSPATH_CONFIGURATION_FILE);
        }
        if (url == null) {
            url = ConfigurationFactory.class.getResource(DEFAULT_CLASSPATH_CONFIGURATION_FILE);
        }
        if (url != null) {
            logger.debug("Configuring redis from redis.xml found in the classpath: " + url);
        }
        Configuration configuration = parseConfiguration(url);
        //configuration.setSource(ConfigurationSource.getConfigurationSource());
        return configuration;
    }


    /**
     * @param inputStream
     * @return a translated stream
     */
    private static String translateSystemProperties(InputStream inputStream) throws IOException {

        StringBuilder sb = new StringBuilder();
        int c;
        Reader reader = new InputStreamReader(inputStream, "UTF-8");
        while ((c = reader.read()) != -1) {
            sb.append((char) c);
        }
        String configuration = sb.toString();

        Set tokens = extractPropertyTokens(configuration);
        for (Object tokenObject : tokens) {
            String token = (String) tokenObject;
            String leftTrimmed = token.replaceAll("\\$\\{", "");
            String trimmedToken = leftTrimmed.replaceAll("\\}", "");

            String property = System.getProperty(trimmedToken);
            if (property == null) {
                logger.debug("Did not find a system property for the " + token +
                        " token specified in the configuration.Replacing with \"\"");
            } else {
                //replaceAll by default clobbers \ and $
                String propertyWithQuotesProtected = Matcher.quoteReplacement(property);
                configuration = configuration.replaceAll("\\$\\{" + trimmedToken + "\\}", propertyWithQuotesProtected);

                logger.debug("Found system property value of " + property + " for the " + token +
                        " token specified in the configuration.");
            }
        }
        return configuration;
    }

    /**
     * Extracts properties of the form ${...}
     *
     * @param sourceDocument the source document
     * @return a Set of properties. So, duplicates are only counted once.
     */
    static Set extractPropertyTokens(String sourceDocument) {
        Set propertyTokens = new HashSet();
        Pattern pattern = Pattern.compile("\\$\\{.+?\\}");
        Matcher matcher = pattern.matcher(sourceDocument);
        while (matcher.find()) {
            String token = matcher.group();
            propertyTokens.add(token);
        }
        return propertyTokens;
    }
}

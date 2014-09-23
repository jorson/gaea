package gaea.platform.security.internal;

import gaea.foundation.core.config.SystemConfig;
import gaea.platform.security.ResourceDefinitionResolver;
import gaea.platform.security.utils.HttpUtil;
import gaea.platform.security.support.ResourceMatchType;
import gaea.platform.security.support.SecurityConstant;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 资源路径的处理类
 * <p/>
 * 路径/aframe/main.do?param_01=pValue&param_02=pValue
 * <p/>
 * 能够被 /aframe/*.do*匹配到
 * <p/>
 * 能够被 /aframe/*.do?param_02=pValue*匹配到
 * <p/>
 * 能够被 /aframe/main.do?param_02=pValue匹配到
 * <p/>
 * 能够被 /aframe/main.do?param_02=pValue&param_01=pValue匹配到
 * <p/>
 *
 * @author wuhy
 */
public class ResourceDefinitionResolverImpl implements ResourceDefinitionResolver {

//    private static Logger logger = Logger.getLogger(ResourceDefinitionResolverImpl.class);

    private PathMatcher pathMatcher = new AntPathMatcher();

//    private final PatternMatcher partternMatcher = new Perl5Matcher();

    private static ResourceMatchType DEFAULT_MATCH_TYPE = ResourceMatchType.ANT;

    private Boolean comparisonIgnoreCase;

    public boolean isResourceMatch(String requestUrl, String resourceUrl) {
        return isResourceMatch(requestUrl, resourceUrl, DEFAULT_MATCH_TYPE);
    }

    public boolean isResourceMatch(String requestUrl, String resourceUrl, ResourceMatchType type) {
        boolean result = false;
        switch (type) {
            case ANT:
                result = pathMatcher.match(resourceUrl, requestUrl);
                break;
            case URL:
                String resUrl = preProcessUrl(resourceUrl);
                String reqUrl = preProcessUrl(requestUrl);
                boolean isMatch = pathMatcher.match(resUrl, reqUrl);
                if (isMatch) {
                    Map<String, String> requestParams = parseURLParameters(requestUrl);
                    Map<String, String> resourceParams = parseURLParameters(resourceUrl);
                    Iterator<Map.Entry<String, String>> resourceIterator = resourceParams.entrySet().iterator();
                    result = true;
                    while (resourceIterator.hasNext()) {
                        Map.Entry<String, String> entry = resourceIterator.next();
                        if (requestParams.containsKey(entry.getKey())) {
                            String value = requestParams.get(entry.getKey());
                            if (!StringUtils.equals(value, entry.getValue())) {
                                result = false;
                                break;
                            }
                        } else {
                            result = false;
                        }
                    }
                }
                break;
//            case REGULAR:
                /*Pattern compiledPattern;
                Perl5Compiler compiler = new Perl5Compiler();
                try {
                    compiledPattern = compiler.compile(resourceUrl, Perl5Compiler.READ_ONLY_MASK);
                } catch (MalformedPatternException mpe) {
                    logger.error("match regular resource happen error,expression:" + resourceUrl, mpe);
                    throw new IllegalArgumentException("Malformed regular expression: " + resourceUrl, mpe);
                }
                result = partternMatcher.matches(requestUrl, compiledPattern);*/
//                break;
        }
        return result;
    }

    /**
     * 获取url参数
     *
     * @param requestUrl
     * @return
     */
    private Map<String, String> parseURLParameters(String requestUrl) {
        Map<String, String> params = new HashMap<String, String>();
        int firstQuestionMarkIndex = requestUrl.lastIndexOf('?');
        if (firstQuestionMarkIndex != -1) {
            String paramsUrl = requestUrl.substring(firstQuestionMarkIndex + 1, requestUrl.length());
            params = HttpUtil.getUrlParams(paramsUrl);
        }
        return params;
    }

    public String preProcessUrl(String url) {
        return preProcessUrl(url, DEFAULT_MATCH_TYPE == ResourceMatchType.ANT, getResourceComparisonIgnoreCase());
    }

    /**
     * 根据是否使用UseAntPath和是否字符小写化来预先格式化url
     *
     * @param url
     * @param isUseAntPath
     * @param ignoreCase
     * @return
     */
    public String preProcessUrl(String url, boolean isUseAntPath, boolean ignoreCase) {
        if (isUseAntPath) {
            // Strip anything after a question mark symbol, as per SEC-161.
            int firstQuestionMarkIndex = url.lastIndexOf("?");
            if (firstQuestionMarkIndex != -1) {
                url = url.substring(0, firstQuestionMarkIndex);
            }
        }
        if (ignoreCase) {
            url = url.toLowerCase();
        }
        return url;
    }

    public boolean getResourceComparisonIgnoreCase() {
        if (comparisonIgnoreCase == null) {
            comparisonIgnoreCase = SystemConfig.Instance.getBooleanProperty(SecurityConstant.RESOURCE_COMPARISON_IGNORE_CASE);
        }
        return comparisonIgnoreCase;
    }
}

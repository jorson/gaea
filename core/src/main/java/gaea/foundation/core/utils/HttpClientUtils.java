package gaea.foundation.core.utils;

import gaea.foundation.core.Constant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Http客户端的工具类
 *
 * @author wuhy
 */
public class HttpClientUtils {

    private static Log logger = LogFactory.getLog(HttpClientUtils.class);

    /**
     * 私有化构造函数，不允许实例化该类
     */
    private HttpClientUtils() {
    }

    /**
     * 通过Http协议及Get方式取得数据
     *
     * @param url URL
     * @return
     * @throws java.io.IOException
     */
    public static String httpGet(String url) throws IOException {
        return httpGet(url, null, null);
    }

    /**
     * 通过Http协议及Get方式取得数据
     *
     * @param url     URL
     * @param charset 字符集
     * @return
     * @throws java.io.IOException
     */
    public static String httpGet(String url, Charset charset) throws IOException {
        return httpGet(url, null, charset);
    }

    /**
     * 通过Http协议及Get方式取得数据
     *
     * @param url        URL
     * @param parameters 参数
     * @return
     * @throws java.io.IOException
     */
    public static String httpGet(String url, Map<String, String> parameters) throws IOException {
        return httpGet(url, parameters, null);
    }

    /**
     * 通过Http协议及Get方式取得数据
     *
     * @param url        URL
     * @param parameters 参数
     * @param charset    字符集
     * @return
     * @throws java.io.IOException
     */
    public static String httpGet(String url, Map<String, String> parameters, Charset charset) throws IOException {
        return httpGet(url, parameters, null, charset);
    }

    /**
     * 通过Http协议及Get方式取得数据
     *
     * @param url        URL
     * @param parameters 参数
     * @param headers    Header参数
     * @param charset    字符集
     * @return
     * @throws java.io.IOException
     */
    public static String httpGet(String url, Map<String, String> parameters, Map<String, String> headers, Charset charset) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String body = null;
        logger.debug("create http get:" + url);
        String destUrl = applyHttpGetParameters(url, parameters);
        HttpGet httpGet = new HttpGet(destUrl);
        body = invoke(httpClient, httpGet, charset);
        httpClient.close();
        return body;
    }

    /**
     * 通过Http协议及Post方式取得数据
     *
     * @param url     URL
     * @param charset 字符集
     * @return
     * @throws java.io.IOException
     */
    public static String httpPost(String url, Charset charset) throws IOException {
        return httpPost(url, null, charset);
    }

    /**
     * 通过Http协议及Post方式取得数据
     *
     * @param url URL
     * @return
     * @throws java.io.IOException
     */
    public static String httpPost(String url) throws IOException {
        return httpPost(url, null, null);
    }

    /**
     * 通过Http协议及Post方式取得数据
     *
     * @param url        URL
     * @param parameters 参数
     * @return
     * @throws java.io.IOException
     */
    public static String httpPost(String url, Map<String, String> parameters) throws IOException {
        return httpPost(url, parameters, null);
    }

    /**
     * 通过Http协议及Post方式取得数据
     *
     * @param url        URL
     * @param parameters 参数
     * @param charset    字符集
     * @return
     * @throws java.io.IOException
     */
    public static String httpPost(String url, Map<String, String> parameters, Charset charset) throws IOException {
        return httpPost(url, parameters, null, charset);
    }

    /**
     * 通过Http协议及Post方式取得数据
     *
     * @param url        URL
     * @param parameters 参数
     * @param headers    Header参数
     * @return
     * @throws java.io.IOException
     */
    public static String httpPost(String url, Map<String, String> parameters, Map<String, String> headers, Charset charset) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String body = null;
        logger.debug("create http post:" + url);
        HttpPost httpPost = new HttpPost(url);
        applyHttpPostParameters(httpPost, parameters, charset);
        applyHeaderParameters(httpPost, headers);
        body = invoke(httpClient, httpPost, charset);
        httpClient.close();
        return body;
    }

    /**
     * 执行发送请求
     *
     * @param httpclient
     * @param httpRequestBase
     * @return
     * @throws java.io.IOException
     */
    private static String invoke(HttpClient httpclient, HttpRequestBase httpRequestBase, Charset charset) throws IOException {
        HttpResponse response = sendRequest(httpclient, httpRequestBase);
        String body = handleResponse(response, charset);
        return body;
    }

    /**
     * 处理返回值
     *
     * @param response
     * @return
     * @throws java.io.IOException
     */
    private static String handleResponse(HttpResponse response, Charset charset) throws IOException {
        logger.debug("get response from http server..");
        HttpEntity entity = response.getEntity();
        logger.debug("response status: " + response.getStatusLine());
        int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
            String body = entity != null ? EntityUtils.toString(entity, charset == null ? Constant.DEFAULT_CHARSET : charset) : null;
            logger.debug(body);
            return body;
        } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
    }

    /**
     * 发送请求
     *
     * @param httpClient
     * @param httpRequestBase
     * @return
     * @throws java.io.IOException
     */
    private static HttpResponse sendRequest(HttpClient httpClient, HttpRequestBase httpRequestBase) throws IOException {
        logger.debug("execute post...");
        // Create a custom response handler
        return httpClient.execute(httpRequestBase);
    }

    /**
     * 设置Get参数
     *
     * @param url
     * @param parameters
     */
    private static String applyHttpGetParameters(String url, Map<String, String> parameters) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        if (!CollectionUtils.isEmpty(parameters)) {
            for (Iterator<Map.Entry<String, String>> iterator = parameters.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, String> data = iterator.next();
                String key = data.getKey();
                String value = data.getValue();
                if (StringUtils.hasText(key)) {
                    sb.append("&");
                    sb.append(key);
                    sb.append("=");
                    sb.append(value);
                }
            }
            if (url.indexOf("?") == -1) {
                sb.replace(0, 1, "?");
            }
            //sb.insert(0, url);
        }
        return url + sb.toString();
    }

    /**
     * 设置Post参数
     *
     * @param httpPost
     * @param parameters
     * @param charset
     */
    private static void applyHttpPostParameters(HttpPost httpPost, Map<String, String> parameters, Charset charset) throws UnsupportedEncodingException {
        if (!CollectionUtils.isEmpty(parameters)) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Iterator<Map.Entry<String, String>> iterator = parameters.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, String> data = iterator.next();
                String key = data.getKey();
                String value = data.getValue();
                if (StringUtils.hasText(key)) {
                    nvps.add(new BasicNameValuePair(key, value));
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, charset == null ? Constant.DEFAULT_CHARSET : charset));
        }
    }

    /**
     * 设置Header参数
     *
     * @param httpRequestBase
     * @param headers
     */
    private static void applyHeaderParameters(HttpRequestBase httpRequestBase, Map<String, String> headers) {
        if (!CollectionUtils.isEmpty(headers)) {
            for (Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, String> data = iterator.next();
                String key = data.getKey();
                String value = data.getValue();
                if (StringUtils.hasText(key)) {
                    httpRequestBase.setHeader(key, value);
                }
            }
        }
    }
}

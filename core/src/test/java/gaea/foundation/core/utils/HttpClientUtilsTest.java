package gaea.foundation.core.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 请在这里输入说明
 *
 * @author wuhy
 */
public class HttpClientUtilsTest {

    @Before
    public void init() {

    }

    @Test
    public void testHttpGet() throws Exception {
        String body = "";
        try {
            body = HttpClientUtils.httpGet("http://11.11.11.11:1111/");
            Assert.assertTrue("发出不存在的请求，方法不报错", false);
        } catch (Exception ex) {
        }
        //body = HttpClientUtils.httpGet("http://developer.51cto.com/art/201103/250028.htm", Charset.forName("gb2312"));
        Map<String, String> params = new HashMap<String, String>();
        params.put("startDate", "2014-10-13");
        params.put("endDate", "2014-10-22");
        //body = HttpClientUtils.httpGet("http://11.11.11.11:1111/client?1=2", params);
        //System.out.println(body);
    }

    @Test
    public void testHttpPost() throws Exception {
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("client_id", "19");
//        map.put("client_secret", "e19a4f1e96d64ef4b5aa7cd356cb1982");
//        map.put("grant_type", "client_credentials");
        //JSONObject ret = HttpUtil.httpPost("http://test.cloud.91open.ty.nd/v1/oauth/token", map);
        //System.out.println(ret);
    }
}

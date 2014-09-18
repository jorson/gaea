package gaea.foundation.core.utils;

import gaea.foundation.core.utils.odata.ODataFilter;
import gaea.foundation.core.utils.odata.ODataPagerQueryParam;
import org.apache.commons.codec.Encoder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 14-9-15.
 */
public class ODataUrlUtils {
    public static final int DEFAULT_PAGE_START = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;
    /**
     * 拼接分页参数
     * @return
     */
    public static String combinePagerUrl(String url,
                                         ODataFilter filter,
                                         ODataPagerQueryParam param) throws UnsupportedEncodingException {
        Assert.notNull(url);
        StringBuilder sb = new StringBuilder(url);
        if(param.getPageIndex()==null){
            param.setPageIndex(DEFAULT_PAGE_START);
        }
        if(param.getPageSize()==null){
            param.setPageSize(DEFAULT_PAGE_SIZE);
        }
        sb.append("?$top="+param.getPageSize()).
                append("&$skip="+(+param.getPageIndex()*+param.getPageSize()));
        if(param.getNeedCount()!=null&&param.getNeedCount()){
            sb.append("&$inlinecount=allpages");
        }
        if(!StringUtils.isEmpty(filter.getExpression())){
            sb.append("&$filter="+ URLEncoder.encode(filter.getExpression(),"UTF-8").replaceAll("\\+","%20"));
        }
        if(!StringUtils.isEmpty(param.getOrderBy())){
            sb.append("&$orderby="+URLEncoder.encode(param.getOrderBy(),"UTF-8").replaceAll("\\+","%20"));
        }
        return sb.toString();
    }

}

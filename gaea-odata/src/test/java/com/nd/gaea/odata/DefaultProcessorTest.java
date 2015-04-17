package com.nd.gaea.odata;

import com.nd.gaea.odata.uri.parser.UriParserException;
import com.nd.gaea.core.repository.query.QuerySupport;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2014-11-21.
 */
public class DefaultProcessorTest {

    @Test
    public void singleFilterTest() throws ODataQueryHandleException, UriParserException, URISyntaxException {

        String orgData = "$filter=birthday eq 2014-10-10T20:14Z and id eq 1";
        //String orgData = "$filter=age lt 15 or (id in (1,2,3) and name eq 'user')";
        //String orgData = "$filter=(a eq 1 and b eq 2) or (c eq 3 or (d eq 4 and e eq 5))";
        MockHttpServletRequest request = new MockHttpServletRequest("get", "/");
        request.setQueryString(orgData);
        ODataQueryHandler handler = ODataQueryHandler.getInstance();
        QuerySupport support = handler.process(request, FilterTest.class);

        Assert.assertNotNull(support);
        //Assert.assertEquals(support.getCriterions().size(), 2);
    }

    @Test
    public void complexFilterTest() throws ODataQueryHandleException, UriParserException {
        String orgData = "$filter=name eq 'test' and id lt 2 or age1 gt 3";
        MockHttpServletRequest request = new MockHttpServletRequest("get", "/");
        request.setQueryString(orgData);
        ODataQueryHandler handler = ODataQueryHandler.getInstance();
        QuerySupport support = handler.process(request, FilterTest.class);

        Assert.assertNotNull(support);
        Assert.assertEquals(support.getCriterions().size(), 3);
    }

    @Test
    public void countTest() throws ODataQueryHandleException, UriParserException {
        String orgData = "$filter=PropertyA eq 1&$count=true";
        MockHttpServletRequest request = new MockHttpServletRequest("get", "/");
        request.setQueryString(orgData);
        ODataQueryHandler handler = ODataQueryHandler.getInstance();
        QuerySupport support = handler.process(request, FilterTest.class);

        Assert.assertNotNull(support);
    }

    @Test
    public void skipAndTopTest() throws ODataQueryHandleException, UriParserException {
        String orgData = "$filter=id eq 1&$offset=10&$limit=20";
        MockHttpServletRequest request = new MockHttpServletRequest("get", "/");
        request.setQueryString(orgData);
        ODataQueryHandler handler = ODataQueryHandler.getInstance();
        QuerySupport support = handler.process(request, FilterTest.class);

        Assert.assertNotNull(support);
    }

    @Test
    public void orderByTest() throws ODataQueryHandleException, UriParserException {
        String orgData = "$filter=id eq 1&$orderby=age2 desc";
        MockHttpServletRequest request = new MockHttpServletRequest("get", "/");
        request.setQueryString(orgData);
        ODataQueryHandler handler = ODataQueryHandler.getInstance();
        QuerySupport support = handler.process(request, FilterTest.class);

        Assert.assertNotNull(support);
    }

    @Test
    public void combineTest() throws ODataQueryHandleException, UriParserException {
        String orgData = "$filter=PropertyInt16 eq 16 and PropertyInt32 eq 32 or PropertyInt64 eq 64&$offset=10&$limit=10&$orderby=PropertyInt32 desc,PropertyInt64 desc&$count=true";
        MockHttpServletRequest request = new MockHttpServletRequest("get", "/");
        request.setQueryString(orgData);
        ODataQueryHandler handler = ODataQueryHandler.getInstance();
        QuerySupport support = handler.process(request, FilterTest.class);

        Assert.assertNotNull(support);
    }

    @Test
    public void dateTransferTest() throws ParseException {
        String date = "2014-10-10";
        String timestamp = "2014-10-10T20:30:15Z";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        Date result = sdf.parse(timestamp);
        Date result2 = sdf2.parse(date);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result2);
    }
}

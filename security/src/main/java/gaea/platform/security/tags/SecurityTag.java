package gaea.platform.security.tags;

import gaea.foundation.core.utils.StringUtils;

import static gaea.platform.security.framework.PurviewJudger.*;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * 权限标签
 * Created by wuhy on 14-7-14.
 */
public class SecurityTag extends SimpleTagSupport {

    private static final String AND = "&&";
    private static final String OR = "||";

    private String value;

    @Override
    public void doTag() throws JspException, IOException {
        if(-1 != value.indexOf(AND)) {
            StringTokenizer stringTokenizer = new StringTokenizer(value, AND);
            while(stringTokenizer.hasMoreTokens()) {
                if(!hasPurview(StringUtils.trimAllWhitespace(stringTokenizer.nextToken()))) {
                    return;
                }
            }
            JspFragment jf = this.getJspBody();
            jf.invoke(null);
            return;
        }
        if(-1 != value.indexOf(OR)) {
            StringTokenizer stringTokenizer = new StringTokenizer(value, OR);
            while(stringTokenizer.hasMoreTokens()) {
                if(hasPurview(StringUtils.trimAllWhitespace(stringTokenizer.nextToken()))) {
                    JspFragment jf = this.getJspBody();
                    jf.invoke(null);
                    return;
                }
            }
            return;
        }
        if(hasPurview(StringUtils.trimAllWhitespace(value))){
            JspFragment jf = this.getJspBody();
            jf.invoke(null);
        }
    }

    public void setValue(String value) {
        this.value = value;
    }
}

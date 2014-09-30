package gaea.platform.batch.communication;

import gaea.foundation.core.utils.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于处理交互报文的工具
 *
 * @author wangchaoxu.
 */
public class CommunicationCommandUtil {

    private static final String REQUEST_REQUEST_ITEM = "Request";
    private static final String REQUEST_PARAMS_ITEM = "Params";

    //获取Job
    public static final String REQUEST_COMMAND_GET_JOB = "GetJob";
    //更新数据
    public static final String REQUEST_COMMAND_UPDATE_DATAS = "UpdateDatas";
    //日志
    public static final String REQUEST_COMMAND_LOG = "Log";
    //失败
    public static final String REQUEST_COMMAND_ONFAIL = "OnFail";
    //成功
    public static final String REQUEST_COMMAND_ONDONE = "OnDone";

    public static final String RESPONSE_VALUE_ITEM = "Value";
    public static final String RESPONSE_JOB_ID_ITEM = "Id";
    public static final String RESPONSE_JOB_TYPE_ITEM = "Identity";
    public static final String RESPONSE_JOB_DATAS_ITEM = "Datas";

    public static final String REQUEST_ONFAIL_MESSAGE_ITEM = "message";
    public static final String REQUEST_ONDONE_MESSAGE_ITEM = "message";
    public static final String REQUEST_LOG_MESSAGE_ITEM = "message";
    public static final String REQUEST_LOG_LEVEL_ITEM = "logLevel";



    private CommunicationCommandUtil() {
    }

    /**
     * 生成请求报文（json）
     * @param cmd
     * @param params
     * @return
     */
    public static String getRequestCommand(String cmd, Map params) {
        Map<String, String> reqMap = new HashMap<String, String>();
        reqMap.put(REQUEST_REQUEST_ITEM, cmd);
        if (params != null) {
            String paramsJsonStr = ObjectUtils.toJson(params);
            reqMap.put(REQUEST_PARAMS_ITEM, paramsJsonStr);
        }
        return ObjectUtils.toJson(reqMap);
    }
}

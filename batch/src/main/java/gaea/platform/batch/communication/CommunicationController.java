package gaea.platform.batch.communication;

import gaea.platform.batch.job.bean.Job;
import gaea.foundation.core.utils.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 交互控制器，定义了各种交互的业务操作
 * 这里定义了N个与.net进程交互的方法，其实可以整合，但是由于他们的属性名首字母大写，导致生成的BEAN变量名怪怪的..所以还是这样写了
 * @author wangchaoxu.
 */
public class CommunicationController {

    private static Log logger = LogFactory.getLog(CommunicationController.class);

    //命名管道的输入输出流对象
    private PipeStream stream;

    public CommunicationController(String readPipeName, String writePipeName) {
        stream = new PipeStream(readPipeName, writePipeName);
    }

    /**
     * 交互操作：获取Job
     * @return
     * @throws java.io.IOException
     */
    public Job getJob() throws IOException {
        String requestStr = CommunicationCommandUtil.getRequestCommand(CommunicationCommandUtil.REQUEST_COMMAND_GET_JOB, null);
        String respenseStr = excuteRequest(requestStr);
        Map<String, String> respenseCmd = ObjectUtils.fromJson(respenseStr, HashMap.class);
        if (respenseCmd != null && respenseCmd.containsKey(CommunicationCommandUtil.RESPONSE_VALUE_ITEM)) {
            String valueStr = respenseCmd.get(CommunicationCommandUtil.RESPONSE_VALUE_ITEM);
            Map valueMap =  ObjectUtils.fromJson(valueStr, HashMap.class);
            Job job = new Job();
            Double idDoubleTmp = Double.valueOf(valueMap.get(CommunicationCommandUtil.RESPONSE_JOB_ID_ITEM).toString());
            job.setId(idDoubleTmp.intValue());
            job.setIdentity((String) valueMap.get(CommunicationCommandUtil.RESPONSE_JOB_TYPE_ITEM));
            String datasStr = valueMap.get(CommunicationCommandUtil.RESPONSE_JOB_DATAS_ITEM).toString();
            if (datasStr != null && !datasStr.isEmpty()) {
                job.setDatas((Map<String, String>) valueMap.get(CommunicationCommandUtil.RESPONSE_JOB_DATAS_ITEM));
            }
            return job;
        }
        return null;
    }

    /**
     * 交互操作：更新数据
     * @param datas
     * @throws java.io.IOException
     */
    public void updateDatas(Map<String, String> datas) throws IOException {
        String requestStr = CommunicationCommandUtil.getRequestCommand(CommunicationCommandUtil.REQUEST_COMMAND_UPDATE_DATAS, datas);
        excuteRequest(requestStr);
    }

    /**
     * 交互操作：作业失败
     * @param message
     * @throws java.io.IOException
     */
    public void onFail(String message) throws IOException {
        Map<String, String> datas = new HashMap<String, String>();
        datas.put(CommunicationCommandUtil.REQUEST_ONFAIL_MESSAGE_ITEM, message);
        String requestStr = CommunicationCommandUtil.getRequestCommand(CommunicationCommandUtil.REQUEST_COMMAND_ONFAIL, datas);
        excuteRequest(requestStr);
    }

    /**
     * 交互操作：作业完成
     * @param message
     * @throws java.io.IOException
     */
    public void onDone(String message) throws IOException {
        Map<String, String> datas = new HashMap<String, String>();
        datas.put(CommunicationCommandUtil.REQUEST_ONDONE_MESSAGE_ITEM, message);
        String requestStr = CommunicationCommandUtil.getRequestCommand(CommunicationCommandUtil.REQUEST_COMMAND_ONDONE, datas);
        excuteRequest(requestStr);
    }

    /**
     * 交互操作：日志
     * @param logLevel
     * @param message
     * @throws java.io.IOException
     */
    public void log(Integer logLevel, String message) throws IOException {
        Map datas = new HashMap();
        datas.put(CommunicationCommandUtil.REQUEST_LOG_LEVEL_ITEM, logLevel);
        datas.put(CommunicationCommandUtil.REQUEST_LOG_MESSAGE_ITEM, message);
        String requestStr = CommunicationCommandUtil.getRequestCommand(CommunicationCommandUtil.REQUEST_COMMAND_LOG, datas);
        excuteRequest(requestStr);
    }

    /**
     * 开启连接
     * @throws java.io.IOException
     */
    public void openConnect() throws IOException {
        stream.open();
    }

    /**
     * 关闭连接
     * @throws java.io.IOException
     */
    public void closeConnect() throws IOException {
        stream.close();
    }

    /**
     * 发送报文并获取返回值
     * @param cmd
     * @return
     * @throws java.io.IOException
     */
    private String excuteRequest(String cmd) throws IOException {
        logger.debug("CommunicationController excuteRequest request string: " + cmd);
        stream.writeString(cmd);
        String ret = null;
        boolean firstTime = true;
        while (ret == null) {
            if (firstTime) {
                firstTime = false;
            } else {
                try {
                    //没读到就每秒钟再读一次，直到读到为止
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error("CommunicationController excuteRequest", e);
                }
            }
            ret = stream.readString();
        }
        logger.debug("CommunicationController excuteRequest response string: " + ret);
        return ret;
    }


}

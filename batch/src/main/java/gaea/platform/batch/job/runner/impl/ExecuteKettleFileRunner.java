package gaea.platform.batch.job.runner.impl;

/**
 * @author wangchaoxu.
 */

import gaea.platform.batch.communication.CommunicationController;
import gaea.platform.batch.job.bean.Job;
import gaea.platform.batch.job.runner.AbstractJobRunner;
import gaea.platform.batch.utils.KettleExecuteUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 执行Kettle生成的文件的Runner
 * @author wangchaoxu.
 */
@Component
public class ExecuteKettleFileRunner extends AbstractJobRunner {

    private static Log logger = LogFactory.getLog(ExecuteKettleFileRunner.class);

    @Override
    public String getJobType() {
        return "executekettlefile";
    }

    @Override
    public void handle(Job job, CommunicationController ctrl) {
        try {
            try {
                Map<String, String> dataMap = job.getDatas();
                String path = dataMap.get("data.ketteFilePath");
                //map里去掉data.ketteFilePath剩下都是参数
                dataMap.remove("data.ketteFilePath");
                String lowerCasePath = path.toLowerCase();
                if (lowerCasePath.endsWith("kjb")) {
                    KettleExecuteUtil.runJob(path, dataMap);
                } else if (lowerCasePath.endsWith("ktr")) {
                    KettleExecuteUtil.runTrans(path, dataMap);
                }
                ctrl.onDone("ExecuteKettleFile Done");
            } catch (Exception e) {
                ctrl.onFail("ExecuteKettleFile Fail");
                throw e;
            }
        } catch (Exception e) {
            logger.error("ExecuteKettleFileRunner handle", e);
        }
    }
}
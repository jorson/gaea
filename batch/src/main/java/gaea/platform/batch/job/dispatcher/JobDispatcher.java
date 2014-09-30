package gaea.platform.batch.job.dispatcher;

import gaea.platform.batch.communication.CommunicationController;
import gaea.platform.batch.job.bean.Job;
import gaea.platform.batch.job.runner.AbstractJobRunner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangchaoxu.
 */
public class JobDispatcher {

    private static Log logger = LogFactory.getLog(JobDispatcher.class);

    private static List<AbstractJobRunner> jobRunnerList = new ArrayList<AbstractJobRunner>();

//    private static List<IJobRunner> jobRunnerList = new ArrayList<IJobRunner>();
//
//    static {
//        //在com.huayu包下寻找实现IJobRunner接口的类
//        List<String> classNameList =  PackageUtil.getClassName("com.huayu");
//        for (String className : classNameList) {
//            Object obj = ClassUtils.createNewInstance(className);
//            if (!(obj instanceof IJobRunner)) {
//                continue;
//            }
//            IJobRunner runner = (IJobRunner)obj;
//            jobRunnerList.add(runner);
//        }
//    }

    //进程交互连接控制器
    private CommunicationController communicationController;

    public JobDispatcher(CommunicationController communicationController) {
        this.communicationController = communicationController;
    }

    /**
     * JobRunner类列表注册
     * @param jobRunner
     */
    public static void jobRunnerRigister(AbstractJobRunner jobRunner) {
        jobRunnerList.add(jobRunner);
    }

    /**
     * 执行任务分发
     * @param job
     */
    public void dispatch(Job job) {
        for (AbstractJobRunner jobRunner : jobRunnerList) {
            // 找到作业类型相同的，执行其handle方法
            if (job.getIdentity().equals(jobRunner.getJobType())) {
                try {
                    jobRunner.handle(job, communicationController);
                } catch (Exception e) {
                    logger.error("JobDispatcher dispatch", e);
                }
                break;
            }
        }
    }
}

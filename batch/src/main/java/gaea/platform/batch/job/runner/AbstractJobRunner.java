package gaea.platform.batch.job.runner;

import gaea.platform.batch.communication.CommunicationController;
import gaea.platform.batch.job.bean.Job;
import gaea.platform.batch.job.dispatcher.JobDispatcher;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author wangchaoxu.
 */
public abstract class AbstractJobRunner implements InitializingBean {
    /**
     * 一被spring实例化立即注册进jobrunner列表
     */
    public final void afterPropertiesSet() {
        JobDispatcher.jobRunnerRigister(this);
    }

    /**
     * 获取作业类型
     * @return
     */
    public abstract String getJobType();

    /**
     * 作业的具体处理
     * @param job
     * @param ctrl
     */
    public abstract void handle(Job job, CommunicationController ctrl);
}

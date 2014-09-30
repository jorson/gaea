package gaea.platform.batch.job.bean;

import java.util.Map;

/**
 * 作业对象
 *
 * @author wangchaoxu.
 */
public class Job {
    //作业ID
    private Integer id;
    //处理能力或叫做作业类型
    private String identity;
    //业务相关数据
    private Map<String, String> datas;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Map<String, String> getDatas() {
        return datas;
    }

    public void setDatas(Map<String, String> datas) {
        this.datas = datas;
    }
}

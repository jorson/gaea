package com.nd.gaea.web.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存成功Controller的返回值
 *
 * @author bifeng.liu
 */
public class SuccessControllerResult extends ControllerResult {

    /**
     * 总记录数
     */
    private long totalCount;
    /**
     * 结果集
     */
    private List result;
    /**
     * 其他的结果
     */
    private Object otherResult;

    public SuccessControllerResult() {
        this(1, "SUCCESS", 0L, new ArrayList());
    }

    public SuccessControllerResult(long totalCount, List result) {
        this(1, "SUCCESS", totalCount, result);
    }

    public SuccessControllerResult(int code, String message, long totalCount, List result) {
        super(code, message);
        this.totalCount = totalCount;
        this.result = result;
    }

    /**
     * 转化成JSON
     *
     * @return
     */
    public String toJson() {
        //return StringUtils.toJson(this);
        return "";
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List getResult() {
        return result;
    }

    public void setResult(List result) {
        this.result = result;
    }

    public Object getOtherResult() {
        return otherResult;
    }

    public void setOtherResult(Object otherResult) {
        this.otherResult = otherResult;
    }
}


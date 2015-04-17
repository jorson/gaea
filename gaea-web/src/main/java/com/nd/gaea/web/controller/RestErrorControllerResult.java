package com.nd.gaea.web.controller;

import java.net.URI;
import java.util.Date;

/**
 * Created by ND on 2015/1/26.
 */
public class RestErrorControllerResult extends ControllerResult {

    /**
     * 错误发生的业务领域
     */
    private String realm;
    /**
     * 发生错误的主机服务id号
     */
    private String hostId;
    /**
     * 请求资源的唯一id
     */
    private String requestId;
    /**
     * 服务器端错误发生的时间
     */
    private Date serverTime;
    /**
     * 再现说明错误类型的文档信息，可选
     */
    private URI type;

    public RestErrorControllerResult() {
        super();
    }

    public RestErrorControllerResult(int code, String message) {
        super(code, message);
    }

    public RestErrorControllerResult(int code, String message, String realm, String hostId, String requestId, Date serverTime, URI type) {
        super(code, message);
        this.realm = realm;
        this.hostId = hostId;
        this.requestId = requestId;
        this.serverTime = serverTime;
        this.type = type;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Date getServerTime() {
        return serverTime;
    }

    public void setServerTime(Date serverTime) {
        this.serverTime = serverTime;
    }

    public URI getType() {
        return type;
    }

    public void setType(URI type) {
        this.type = type;
    }
}

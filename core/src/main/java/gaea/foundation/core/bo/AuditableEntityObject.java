package gaea.foundation.core.bo;

import java.util.Date;

/**
 * 可审计实体抽象父类
 * <p/>
 * 可审计实体不允许在物理表删除，使用状态来保存其记录是否有效
 * 要保存记录是由谁添加、修改的
 *
 * @author wuhy
 */
public abstract class AuditableEntityObject extends EntityObject {
    /**
     * 有效值
     */
    public static final int STATUS_VALID_VALUE = 1;
    /**
     * 状态
     */
    private int status = STATUS_VALID_VALUE;
    /**
     * 新增人员
     */
    private String insertUser;
    /**
     * 新增时间
     */
    private Date insertDate;
    /**
     * 修改人员
     */
    private String updateUser;
    /**
     * 修改时间
     */
    private Date updateDate;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInsertUser() {
        return insertUser;
    }

    public void setInsertUser(String insertUser) {
        this.insertUser = insertUser;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}

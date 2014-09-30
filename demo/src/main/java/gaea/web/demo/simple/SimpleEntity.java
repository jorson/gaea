package gaea.web.demo.simple;

import gaea.foundation.core.bo.AuditableEntityObject;

import java.util.Date;

/**
 * Created by Administrator on 14-9-30.
 */
public class SimpleEntity extends AuditableEntityObject {

    public SimpleEntity() {

    }

    public SimpleEntity(String name, Date birthday, int age, int status, String remark) {
        this.name = name;
        this.birthday = birthday;
        this.age = age;
        this.setStatus(status);
        this.remark = remark;
    }

    private String name;
    private Date birthday;
    private Integer age;
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

package com.nd.gaea.repository.hibernate.object;

import java.util.Date;

/**
 * 请在这里输入说明
 *
 * @author bifeng.liu
 */
public class SimpleEntity {
    public SimpleEntity() {

    }

    public SimpleEntity(String name, Date birthday, int age, int status, String remark) {
        this.name = name;
        this.birthday = birthday;
        this.age = age;
        this.status = status;
        this.remark = remark;
    }

    private Long id;
    private String name;
    private Date birthday;
    private int status;
    private int age;
    private String remark;
    private JsonEntity jsonEntity;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public JsonEntity getJsonEntity() {
        return jsonEntity;
    }

    public void setJsonEntity(JsonEntity jsonEntity) {
        this.jsonEntity = jsonEntity;
    }
}

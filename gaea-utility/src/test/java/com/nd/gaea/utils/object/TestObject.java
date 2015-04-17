package com.nd.gaea.utils.object;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 测试对象
 *
 * @author bifeng.liu
 */
public class TestObject implements Serializable {
    private long id;
    private String name;
    private Date birthday;
    private List<Object> hobby;
    private String[] projects;
    private Map<String, Object> attributes;

    public TestObject() {

    }

    public TestObject(long id, String name, Date birthday) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public List<Object> getHobby() {
        return hobby;
    }

    public void setHobby(List<Object> hobby) {
        this.hobby = hobby;
    }

    public String[] getProjects() {
        return projects;
    }

    public void setProjects(String[] projects) {
        this.projects = projects;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public String doParentMethod() {
        return "doParentMethod";
    }

    public boolean doMethod(TestObject testObject) {
        return false;
    }

    public String addName(String message) {
        return name + message;
    }
}

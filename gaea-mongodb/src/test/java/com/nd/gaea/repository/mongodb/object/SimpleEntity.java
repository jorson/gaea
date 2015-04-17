package com.nd.gaea.repository.mongodb.object;

import java.util.Date;

/**
 * 请在这里输入说明
 *
 * @author bifeng.liu
 */
public class SimpleEntity {

    private String id;
    private String name;
    private int age;
    private int type;
    private Date createDate;

    public SimpleEntity(String name, int age, int type, Date createDate) {
        this.age = age;
        this.name = name;
        this.type = type;
        this.createDate = createDate;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleEntity that = (SimpleEntity) o;

        if (age != that.age) return false;
        if (type != that.type) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + age;
        result = 31 * result + type;
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        return result;
    }
}

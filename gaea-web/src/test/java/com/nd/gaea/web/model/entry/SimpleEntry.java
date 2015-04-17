package com.nd.gaea.web.model.entry;

import java.util.Date;

/**
 * Created by Administrator on 2014-11-25.
 */
public class SimpleEntry {

    private Long Id;
    private String Name;
    private String password;
    private Integer age;
    private Date birthday;
    private NestEntry nestEntry;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public NestEntry getNestEntry() {
        return nestEntry;
    }

    public void setNestEntry(NestEntry nestEntry) {
        this.nestEntry = nestEntry;
    }
}

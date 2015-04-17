package com.nd.gaea.web.converter.object;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Json 实体
 * <p/>
 * 用于测试Json
 *
 * @author bifeng.liu
 * @since 2014/11/28
 */
public class JsonEntity {
    private Object id;
    private Date birthday;
    private String jsonName;
    private int jsonCode;
    private List<String> values;
    private EnumType enumType;
    private Map<String, Object> attributes;

    private SubJsonEntity subJsonEntity;

    public JsonEntity() {
        this.values = new ArrayList<String>();
    }

    public JsonEntity(String jsonName, int jsonCode) {
        this.jsonName = jsonName;
        this.jsonCode = jsonCode;
        this.values = new ArrayList<String>();
    }

    public JsonEntity(String jsonName, int jsonCode, List<String> values) {
        this.jsonName = jsonName;
        this.jsonCode = jsonCode;
        this.values = values;
    }

    public Long getId() {
        return (Long) id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getJsonName() {
        return jsonName;
    }

    public void setJsonName(String jsonName) {
        this.jsonName = jsonName;
    }

    public int getJsonCode() {
        return jsonCode;
    }

    public void setJsonCode(int jsonCode) {
        this.jsonCode = jsonCode;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public SubJsonEntity getSubJsonEntity() {
        return subJsonEntity;
    }

    public void setSubJsonEntity(SubJsonEntity subJsonEntity) {
        this.subJsonEntity = subJsonEntity;
    }

    public EnumType getEnumType() {
        return enumType;
    }

    public void setEnumType(EnumType enumType) {
        this.enumType = enumType;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Object getData() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonEntity that = (JsonEntity) o;

        if (jsonCode != that.jsonCode) return false;
        if (!jsonName.equals(that.jsonName)) return false;
        if (!values.equals(that.values)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = jsonName.hashCode();
        result = 31 * result + jsonCode;
        result = 31 * result + values.hashCode();
        return result;
    }


}

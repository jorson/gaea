package com.nd.gaea.repository.hibernate.object;

import java.util.ArrayList;
import java.util.List;

/**
 * Json 实体
 * <p/>
 * 用于测试Json UserType
 *
 * @author bifeng.liu
 * @since 2014/11/28
 */
public class JsonEntity {
    private String jsonName;
    private int jsonCode;
    private List<String> values;

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

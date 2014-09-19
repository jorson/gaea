package gaea.foundation.core.utils.object;

import java.util.Date;
import java.util.List;

/**
 * 请在这里输入说明
 *
 * @author wuhy
 */
public class JsonObject {
    private long id;
    private String name;
    private Date birthday;
    private List<String> hobby;
    private String[] projects;
//    private EnumObject enumObject;

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

    public List<String> getHobby() {
        return hobby;
    }

    public void setHobby(List<String> hobby) {
        this.hobby = hobby;
    }

    public String[] getProjects() {
        return projects;
    }

    public void setProjects(String[] projects) {
        this.projects = projects;
    }

//    public EnumObject getEnumObject() {
//        return enumObject;
//    }
//
//    public void setEnumObject(EnumObject enumObject) {
//        this.enumObject = enumObject;
//    }
}

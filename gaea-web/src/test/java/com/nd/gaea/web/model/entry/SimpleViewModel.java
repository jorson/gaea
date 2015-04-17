package com.nd.gaea.web.model.entry;

import com.google.gson.reflect.TypeToken;
import com.nd.gaea.core.model.ViewModel;
import com.nd.gaea.core.model.ViewModelMapping;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Administrator on 2014-11-25.
 */
@ViewModelMapping(sourceType = SimpleEntry.class)
public class SimpleViewModel implements ViewModel {

    private Long Id;
    private String Name;
    private Integer age;
    private NestViewModel nestViewModel;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public NestViewModel getNestViewModel() {
        return nestViewModel;
    }

    public void setNestViewModel(NestViewModel nestViewModel) {
        this.nestViewModel = nestViewModel;
    }

    @Override
    public Type genListViewModelType() {
        return new TypeToken<List<SimpleViewModel>>(){}.getType();
    }
}

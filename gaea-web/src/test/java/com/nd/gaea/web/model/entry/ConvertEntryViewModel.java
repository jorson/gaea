package com.nd.gaea.web.model.entry;

import com.google.gson.reflect.TypeToken;
import com.nd.gaea.core.model.ViewModel;
import com.nd.gaea.core.model.ViewModelMapping;
import com.nd.gaea.web.model.convert.SimpleConverter;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Administrator on 2014-11-26.
 */
@ViewModelMapping(sourceType = SimpleEntry.class, converter = SimpleConverter.class)
public class ConvertEntryViewModel implements ViewModel {

    private Long Id;
    private String Name;
    private Integer age;

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

    @Override
    public Type genListViewModelType() {
        return new TypeToken<List<ConvertEntryViewModel>>(){}.getType();
    }
}

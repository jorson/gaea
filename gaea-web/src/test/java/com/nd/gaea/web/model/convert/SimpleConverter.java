package com.nd.gaea.web.model.convert;

import com.nd.gaea.core.model.converter.AbstractModelConverter;
import com.nd.gaea.web.model.entry.ConvertEntryViewModel;
import com.nd.gaea.web.model.entry.SimpleEntry;

/**
 * Created by Administrator on 2014-11-26.
 */
public class SimpleConverter extends AbstractModelConverter<SimpleEntry, ConvertEntryViewModel> {

    @Override
    public ConvertEntryViewModel convert(SimpleEntry source) {
        ConvertEntryViewModel result = new ConvertEntryViewModel();
        result.setId(source.getId());
        result.setName(source.getName() + source.getPassword());
        result.setAge(source.getAge());
        return result;
    }
}

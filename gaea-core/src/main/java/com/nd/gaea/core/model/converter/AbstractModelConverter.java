package com.nd.gaea.core.model.converter;

import com.nd.gaea.core.model.ViewModel;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public abstract class AbstractModelConverter<S, D extends ViewModel> implements Converter<S, D> {

    @Override
    public D convert(MappingContext<S, D> context) {
        return convert(context.getSource());
    }

    protected abstract D convert(S source);
}

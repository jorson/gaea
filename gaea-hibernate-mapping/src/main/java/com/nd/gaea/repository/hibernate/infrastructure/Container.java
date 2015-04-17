package com.nd.gaea.repository.hibernate.infrastructure;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jorson.WHY
 * @package com.nd.demo.infrastructure
 * @since 2015-03-23
 */
public class Container {

    private final Map<Class, Object> registeredTypes =
            new HashMap<Class, Object>();

    public void register(Object xmlWriter, Class registerClazz) {
        if(!registeredTypes.containsKey(registerClazz)) {
            registeredTypes.put(registerClazz, xmlWriter);
        }
    }

    public Object resolve(Class clazz) throws ResolveException {
        if(!registeredTypes.containsKey(clazz)) {
            throw new ResolveException(clazz);
        }
        return registeredTypes.get(clazz);
    }
}

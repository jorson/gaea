package com.nd.gaea.odata.api.uri;

import java.lang.reflect.Type;

/**
 * Used to describe an typed resource part (super interface)
 */
public interface UriResourcePartTyped extends UriResource {

    /**
     * @return Type of the resource part
     */
    Type getType();

    /**
     * @return True if the resource part is a collection, otherwise false
     */
    boolean isCollection();

    /**
     * @return String representation of the type
     */
    String toString(boolean includeFilters);

}

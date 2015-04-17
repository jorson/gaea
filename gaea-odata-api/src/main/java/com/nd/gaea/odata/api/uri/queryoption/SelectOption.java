package com.nd.gaea.odata.api.uri.queryoption;

import java.util.List;

/**
 * Represents the system query option $select
 * For example: http://.../entitySet?select=name,age
 */
public interface SelectOption extends SystemQueryOption {

    /**
     * @return A list of select items used in $select
     */
    List<SelectItem> getSelectItems();

}

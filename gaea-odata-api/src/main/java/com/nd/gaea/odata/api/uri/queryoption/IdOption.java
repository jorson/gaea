package com.nd.gaea.odata.api.uri.queryoption;

/**
 * Represents the system query option $id when using a entity-id to resolve the entity
 * For example: http://.../$entity?$id=Products(0)
 */
public interface IdOption extends SystemQueryOption {

    /**
     * @return Value of $id
     */
    String getValue();

}

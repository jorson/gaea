package gaea.platform.web.exception;

import java.io.Serializable;

/**
 * 未找到对象异常类
 *
 * @author bifeng.liu
 */
public class ObjectNotFoundException extends ControllerException {
    private final Object identifier;
    private final String entityName;

    /**
     * Constructs an UnresolvableObjectException using the specified information.
     *
     * @param identifier The identifier of the entity which could not be resolved
     * @param entityName The name of the entity which could not be resolved
     */
    public ObjectNotFoundException(Object identifier, String entityName) {
        this("No row with the given identifier exists", identifier, entityName);
    }

    protected ObjectNotFoundException(String message, Object identifier, String clazz) {
        super(message);
        this.identifier = identifier;
        this.entityName = clazz;
    }

    /**
     * Generate an info message string relating to a particular entity,
     * based on the given entityName and id.
     *
     * @param entityName The defined entity name.
     * @param id         The entity id value.
     * @return An info string, in the form [FooBar#1].
     */
    public static String infoString(String entityName, Serializable id) {
        StringBuilder s = new StringBuilder();
        s.append('[');
        if (entityName == null) {
            s.append("<null entity name>");
        } else {
            s.append(entityName);
        }
        s.append('#');

        if (id == null) {
            s.append("<null>");
        } else {
            s.append(id);
        }
        s.append(']');
        return s.toString();
    }
}

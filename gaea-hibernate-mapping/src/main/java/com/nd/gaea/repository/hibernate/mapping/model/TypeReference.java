package com.nd.gaea.repository.hibernate.mapping.model;

/**
 *
 * @author jorson.WHY
 * @package com.nd.demo.mapping.model
 * @since 2015-03-25
 */
public class TypeReference {

    public static final TypeReference empty = new TypeReference("nop");

    private Class innerClazz;
    private String innerName;

    public TypeReference(String name) {
        try {
            this.innerClazz = Class.forName(name, false, null);
        } catch (ClassNotFoundException e) {
            if(name.equals("nop")) {
                //Do Nothing;
            } else {
                e.printStackTrace();
            }

        }
        this.innerName = name;
    }

    public TypeReference(Class clazz) {
        innerClazz = clazz;
        innerName = clazz.getName();
    }

    public String getInnerName() {
        return innerName;
    }

    @Override
    public String toString() {
        if(innerClazz == null) {
            return innerName;
        } else {
            return innerClazz.getName();
        }
    }
}

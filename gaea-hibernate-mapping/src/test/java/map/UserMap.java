package map;

import entry.User;
import com.nd.gaea.repository.hibernate.mapping.ClassMap;

/**
 *
 * @author jorson.WHY
 * @package com.nd.demo.map
 * @since 2015-03-31
 */
public class UserMap extends ClassMap<User> {

    public UserMap() {
        table("demo_user");
        id(getField("id"), "user_id").getGeneratedBy().identity();
        map(getField("name"), "user_name");
        map(getField("age"), "user_age");
    }
}

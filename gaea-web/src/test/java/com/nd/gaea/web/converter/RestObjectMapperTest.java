package com.nd.gaea.web.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nd.gaea.web.converter.object.EnumType;
import com.nd.gaea.web.converter.object.JsonEntity;
import com.nd.gaea.web.converter.object.SubJsonEntity;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RestObjectMapperTest {

    @Test
    public void testToJson() {
        RestObjectMapper restObjectMapper = new RestObjectMapper();
        Map<String, Object> ret = new HashMap<>();
        JsonEntity jsonEntity = new JsonEntity("测试", 1);
        jsonEntity.setBirthday(new Date());
        SubJsonEntity subJsonEntity = new SubJsonEntity("123123", 12313);
        jsonEntity.setSubJsonEntity(subJsonEntity);
        jsonEntity.setEnumType(EnumType.INT);
        Map<String,Object> attributes = new HashMap<>();
        attributes.put("testName","aaa");
        attributes.put("testObject","cccc");
        attributes.put("test_Object","c测试c");
        jsonEntity.setAttributes(attributes);
        ret.put("simpleEntity", jsonEntity);
        ret.put("testName","测试名称a!@#$!@$&^%^()\"");
        ret.put("test_aaaame","adasfasfd");
        ret.put("Taaaaesa","adasfasfd");
        try {
            System.out.println(restObjectMapper.writeValueAsString(ret));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFromJson() {
        String jsonString = "{\"id\":null,\"aa\":null,\"birthday\":\"2014-05-12T12:12:11Z\",\"enum_type\":\"2\",\"json_name\":\"测试\",\"json_code\":1,\"values\":[],\"sub_json_entity\":{\"sub_json_name\":\"123123\",\"sub_json_code\":12313}}";
        RestObjectMapper restObjectMapper = new RestObjectMapper();

        try {
            JsonEntity jsonEntity = restObjectMapper.readValue(jsonString, JsonEntity.class);
            System.out.println(jsonEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
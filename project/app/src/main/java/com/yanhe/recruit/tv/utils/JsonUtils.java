package com.yanhe.recruit.tv.utils;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 *
 * @author zhl
 * @create: 2018-04-24 17:20
 **/

public class JsonUtils {

    public static final ObjectMapper mapper = new ObjectMapper();



/**转化为josn字符串（序列化）*/

    public static String serialize(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass() == String.class) {
            return (String) obj;
        }
        try {
            return mapper.writeValueAsString(obj);
        }
        catch (IOException e) {
            Logger.e("json序列化出错：" + obj, e);
            return null;
        }
    }

    public static <T> T parse(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            Logger.e("来了");
            Logger.e("json解析出错：" + json, e);
            e.printStackTrace();
            return null;
        }
    }


/**Class<E> eClass 元素类型*/

    public static <E> List<E> parseList(String json, Class<E> eClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (IOException e) {
            Logger.e("json解析出错：" + json, e);
            return null;
        }
    }

    public static <K, V> Map<K, V> parseMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (IOException e) {
            Logger.e("json解析出错：" + json, e);
            return null;
        }
    }


/**泛型 T 是什么类型就返回什么类型*/

    public static <T> T nativeRead(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            Logger.e("json解析出错：" + json, e);
            return null;
        }
    }


/* @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class User{
        String name;
        Integer agr;
    }

    public static void main(String[] args) {
        User user = new User("jack", 21);
        //序列化
        String json=serialize(user);
        //反序列化
        User user1 = parse(json, User.class);
        System.out.println("user1："+user1);

        //toList
        json="[20,10,-15,12]";
        List<Integer> lists = parseList(json, Integer.class);
        System.out.println("list:"+lists);

        //toMap
        json="{\"name\":\"jack\",\"age\":\"21\"}";
        Map<String, String> map = parseMap(json, String.class, String.class);
        System.out.println("map:"+map);

        json="[{\"name\":\"jack\",\"age\":\"21\"},{\"name\":\"Rose\",\"age\":\"18\"}]";
        List<Map<String, String>> maps = nativeRead(json, new TypeReference<List<Map<String, String>>>() {
        });
        for (Map<String, String> map1 : maps) {
            System.out.println("map1="+map1);
        }
    }*/

}

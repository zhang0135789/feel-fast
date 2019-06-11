package com.feel.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

/**
 * @Author: zz
 * @Description:
 * @Date: 下午 4:58 2018/12/3 0003
 * @Modified By
 */
public class JsonUtil {
    /**
     * ObjectMapper
     */
    private static final ObjectMapper objectMapper = createObjectMapper();

    /**
     * 序列化成json字符串
     *
     * @param obj 对象
     * @return json 字符串
     * @throws IOException 异常
     */
    public static String toJson(Object obj) throws IOException {
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * 将json字符串反序列化成对象
     *
     * @param json      json字符串
     * @param valueType 对象类型
     * @param <T>       对象类型
     * @return 对象
     * @throws IOException 异常
     */
    public static <T> T fromJson(String json, Class<T> valueType) throws IOException {
        return objectMapper.readValue(json, valueType);
    }

    /**
     * 将json字符串反序列化成对象
     *
     * @param json json json字符串
     * @param ref  对象类型
     * @param <T>  对象类型
     * @return 对象
     * @throws IOException 异常
     */
    public static <T> T fromJson(String json, TypeReference<T> ref) throws IOException {
        return objectMapper.readValue(json, ref);
    }


    /**
     * 创建ObjectMapper
     *
     * @return ObjectMapper
     */
    private static ObjectMapper createObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}

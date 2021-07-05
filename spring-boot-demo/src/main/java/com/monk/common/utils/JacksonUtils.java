/**
 * 
 * 文件名：JacksonUtils.java
 * 版权： Copyright 2017-2022 CMCC All Rights Reserved.
 * 描述： ESB管理系统
 */
package com.monk.common.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 模拟可重入锁写的一个demo类<br/>
 * 
 * @author Monk
 * @version V1.0
 * @date 2021年7月1日 上午11:17:58
 */
public class JacksonUtils {

    private final JacksonObjectMapper mapper;

    public String writeValueAsString(Object obj) {
        try {
            return mapper.getInstance().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JacksonUtils() {
        mapper = new WriteNull();
    }

    public JacksonUtils(boolean writeValueAsEmpty) {
        mapper = writeValueAsEmpty ? new WriteNull() : new WriteNullAsEmpty();
    }

    abstract static class JacksonObjectMapper {

        abstract ObjectMapper getInstance();

    }

    static final class WriteNullAsEmpty extends JacksonObjectMapper {

        @Override
        ObjectMapper getInstance() {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
                @Override
                public void serialize(Object arg0, JsonGenerator arg1, SerializerProvider arg2)
                        throws IOException {
                    arg1.writeString("");
                }
            });
            return objectMapper;
        }

    }

    static final class WriteNull extends JacksonObjectMapper {
        @Override
        ObjectMapper getInstance() {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper;
        }
    }

}

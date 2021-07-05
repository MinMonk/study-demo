/**
 * 
 * 文件名：JacksonTest.java
 * 版权： Copyright 2017-2022 CMCC All Rights Reserved.
 * 描述： ESB管理系统
 */
package com.monk.utils.jackson;

import java.io.IOException;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.monk.bean.Person;
import com.monk.common.utils.JacksonUtil;
import com.monk.common.utils.JacksonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年6月30日 下午3:27:42
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@TestInstance(Lifecycle.PER_CLASS)
public class JacksonTest {
    
    @BeforeAll
    public void runBeforeAllTestMethod() {
        log.info("@BeforeAll - runBeforeAllTestMethod");
    }
    
    @BeforeEach
    public void runBeforeEachTestMethod() {
        log.info("@BeforeEach - runBeforeEachTestMethod");
    }
    
    @AfterAll
    public void runAfterAllTestMethod() {
        log.info("@AfterAll - runAfterAllTestMethod");
    }
    
    @AfterEach
    public void runAfterEachTestMethod() {
        log.info("@AfterEach - runAfterEachTestMethod");
    }
 
    @Test
    public void writeValueAsString() {
        Person per = new Person(1L, null, 18, new Date());
        String str = JacksonUtil.writeValueAsString(per);
        log.info("writeValueAsString-->{}", str);
    }
    
    @Test
    public void writeNullAsEmpty() throws JsonProcessingException {
        ObjectMapper objMappper = new ObjectMapper();
        /**
         * 将null对象输出为空字符串""
         */
        objMappper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>(){
            @Override
            public void serialize(Object arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
                arg1.writeString("");
            }
        });
        
        Person person = new Person(1L, null, 18, new Date());
        String str = objMappper.writeValueAsString(person);
        log.info("writeNullAsEmpty-->{}", str);
    }
    
    @Test
    public void writeNullAsEmptyTest() {
        JacksonUtils util = new JacksonUtils(false);
        Person person = new Person(1L, null, 18, new Date());
        String str = util.writeValueAsString(person);
        log.info("write null as empty --> {}", str);
    }
    
    @Test
    public void writeNullTest() {
        JacksonUtils util = new JacksonUtils();
        Person person = new Person(1L, null, 18, new Date());
        String str = util.writeValueAsString(person);
        log.info("write null --> {}", str);
    }

}

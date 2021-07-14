package com.monk.common.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 格式化Json工具类
 * <p>
 * 当前JsonUtil类支持一些com.fasterxml.jackson.annotation下的注解 <br/>
 * 比如 : @JsonFormat @JsonIgnore @JsonProperty 等等...
 * </p>
 * 
 * @author Monk
 * @version V1.0
 * @date 2021年3月31日 上午11:44:46
 */
public class JacksonUtil {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);

    private static ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 获取jackson ObjectMapper对象
     * 
     * @return ObjectMapper对象
     * @date 2021年4月7日 下午3:02:45
     */
    public static ObjectMapper getInstance() {
        
        /**
         * 如果需要将null的属性输出为""空字符串，那么就需要setNullValueSerializer
         */
        /*objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>(){
            @Override
            public void serialize(Object arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
                arg1.writeString("");
            }
        });*/
        
        return objectMapper;
    }

    /**
     * 格式化对象为jsonStr，支持注解
     * 
     * @param obj
     *            对象
     * @return 格式化后的jsonStr
     * @author Monk
     * @date 2021年3月31日 上午11:22:27
     */
    public static String writeValueAsString(Object obj) {
        try {
            return getInstance().writeValueAsString(obj);
        } catch (JsonGenerationException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }
    
    /**
     * 将json字符串解析为实体对象bean、Map、List(array)
     * 
     * @param jsonStr
     *            json 字符串
     * @param clazz
     *            实体对象类
     * @return 实体对象
     * @date 2021年4月7日 下午3:04:23
     */
    public static <T> T readValue(String jsonStr, Class<T> clazz) {
        try {
            return getInstance().readValue(jsonStr, clazz);
        } catch (JsonParseException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    
    /**
     * 将json字符串解析为集合实体对象（List<Bean>）
     * @param jsonStr
     *          json字符串
     * @param parametrized
     *          参数类型
     * @param parameterClasses
     *          参数类型集合
     * @return
     *          集合实体对象（List<Bean>）
     * @date 2021年4月7日 下午3:09:22
     */
    public static <T> T readValue(String jsonStr, Class<?> parametrized, Class<?>... parameterClasses) {
        try {
            JavaType javaType = getInstance().getTypeFactory().constructParametricType(parametrized, parameterClasses);
            return getInstance().readValue(jsonStr, javaType);
        } catch (JsonParseException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}

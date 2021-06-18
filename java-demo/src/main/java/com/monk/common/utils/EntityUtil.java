/**
 * 
 * 文件名：EntityUtil.java
 * 版权： Copyright 2017-2022 CMCC All Rights Reserved.
 * 描述： ESB管理系统
 */
package com.monk.common.utils;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实体工具类
 * 
 * @author Monk
 * @version V1.0
 * @date 2021年3月26日 下午7:01:12
 */
public class EntityUtil {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(EntityUtil.class);

    /**
     * 类全限定名：Integer
     */
    private static final String INTEGER_CLASS_NAME = "java.lang.Integer";

    /**
     * 类全限定名：Long
     */
    private static final String LONG_CLASS_NAME = "java.lang.Long";

    /**
     * 类全限定名：Date
     */
    private static final String DATE_CLASS_NAME = "java.util.Date";

    /**
     * 将Map中的结果集转换为对应的实体对象
     * 
     * @param data
     *            Map中的结果集
     * @param clazz
     *            实体类
     * @return 实体对象
     * @author huangyulan
     * @date 2021年3月16日 上午11:03:34
     */
    public static <T> Object convertEntityByMap(Map<String, Object> data, Class<T> clazz) {
        T obj = null;
        try {
            obj = clazz.newInstance();
            for (Field field : obj.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column col = field.getAnnotation(Column.class);
                    Object value = data.get(col.name());
                    if (null == value) {
                        continue;
                    }
                    Class<?> clz = field.getType();
                    field.setAccessible(true);
                    if (INTEGER_CLASS_NAME.equals(clz.getName())) {
                        field.set(obj, (Integer) value);
                    } else if (LONG_CLASS_NAME.equals(clz.getName())) {
                        field.set(obj, Long.valueOf(value.toString()));
                    } else if (DATE_CLASS_NAME.equals(clz.getName())) {
                        if (value instanceof Timestamp) {
                            Timestamp tp = (Timestamp) value;
                            field.set(obj, new Date(tp.getTime()));
                        } else {
                            logger.info("class:{}, value:{}", value.getClass().getName(), value);
                        }
                    } else {
                        field.set(obj, value);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return obj;
    }

}

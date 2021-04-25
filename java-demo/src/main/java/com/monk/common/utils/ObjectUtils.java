/**
 * 
 * 文件名：ObjectUtils.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.common.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Monk
 * @version V1.0
 * @date 2019年5月29日 上午10:57:41
 */
public class ObjectUtils {

    public static Logger logger = LoggerFactory.getLogger(ObjectUtils.class);

    /**
     * 
     * 将object类型转成String字符串
     * 
     * @param obj
     *            源对象
     * @return 对象的字符串
     * @author Monk
     * @date 2019年5月25日 下午20:28:59
     */
    public static String praseObjectToString(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return obj.toString();
        }
    }

    /**
     * 将object类型转为BigDecimal类型
     * 
     * @param value
     *            源对象
     * @return BigDecimal类型
     * @author Monk
     * @date 2019年5月28日 下午4:27:28
     */
    public static BigDecimal praseObjectToBigDecimal(Object value) {
        BigDecimal ret = null;
        if (value != null) {
            if (value instanceof BigDecimal) {
                ret = (BigDecimal) value;
            } else if (value instanceof String) {
                ret = new BigDecimal((String) value);
            } else if (value instanceof BigInteger) {
                ret = new BigDecimal((BigInteger) value);
            } else if (value instanceof Number) {
                ret = new BigDecimal(((Number) value).doubleValue());
            } else {
                throw new ClassCastException("Not possible to coerce [" + value + "] from class "
                        + value.getClass() + " into a BigDecimal.");
            }
        }
        return ret;
    }

    /**
     * 
     * 解析对象到长整形
     * 
     * @param obj
     *            源对象
     * @return 对象的长整型的值
     * @author Monk
     * @date 2019年5月28日 下午4:27:28
     */
    public static Long praseObjectToLong(Object obj) {
        if (obj == null) {
            return 0L;
        }

        if (obj instanceof BigDecimal) {
            BigDecimal bigVal = (BigDecimal) obj;
            return bigVal.longValue();
        } else if (obj instanceof Long) {
            return (Long) obj;
        } else if (obj instanceof Integer) {
            Integer intVal = (Integer) obj;
            return intVal.longValue();
        } else {
            return 0L;
        }
    }
}

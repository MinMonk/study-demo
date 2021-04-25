/**
 * 
 * 文件名：FormatJson.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.monk.demo.lambda.User;


/**
 *
 * @author Monk
 * @version V1.0
 * @date 2020年12月28日 上午10:03:07
 */
public class FormatJson {
    public static void main(String[] args) {
        User user = new User();
        user.setId(1L);
        
        format(user);
        
    }
    
    
    private static void format(Object obj) {
        System.out.println(JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm:ss", new SerializerFeature[] {
                SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteMapNullValue, 
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteNullListAsEmpty}));
    }

}

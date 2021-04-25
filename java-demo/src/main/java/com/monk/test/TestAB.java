/**
 * 
 * 文件名：TestAB.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.test;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年1月20日 下午3:33:23
 */
public class TestAB {
    public static void main(String[] args) {
        A a = new A();
        a.setId(1);
        a.setName("jack");
        System.out.println(a);
        
        B b = new B(a);
        b.setAge(12);
        System.out.println(b);
        
        Set<String> set = new HashSet<String>();
        System.out.println("---" + String.join(",", set));
        
    }

}

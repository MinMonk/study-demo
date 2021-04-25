/**
 * 
 * 文件名：A.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.test;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年1月20日 下午3:32:02
 */
public class A {
    
    private int id;
    private String name;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "A [id=" + id + ", name=" + name + "]";
    }

}

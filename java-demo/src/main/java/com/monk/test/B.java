/**
 * 
 * 文件名：B.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.test;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年1月20日 下午3:32:46
 */
public class B extends A {
    
    public B(A a) {
        super.setId(a.getId());
        super.setName(a.getName());
    }
    
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "B [id=" + super.getId() + ", name=" + super.getName() + ", age=" + age + "]";
    }
}

/**
 * 
 * 文件名：Person.java
 * 版权： Copyright 2017-2022 CMCC All Rights Reserved.
 * 描述： ESB管理系统
 */
package com.monk.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;


/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年6月30日 下午2:53:55
 */
public class Person implements Serializable{
    
    private static final long serialVersionUID = 7840790657047001043L;

    private Long id;
    
    //@JsonInclude(JsonInclude.Include.NON_NULL) 
    private String name;
    
    private int age;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date date;
    
    public Person() {}
    
    public Person(Long id, String name, int age, Date date) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}

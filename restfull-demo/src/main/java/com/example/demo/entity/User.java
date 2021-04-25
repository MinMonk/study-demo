package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.Date;

public class User {

    private Long id;

    private String name;

    private Integer age;

    private String remark;

    private String sex;

    private String enabledFlag;

    private String createDate;

    private String updateDate;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public User(){

    }

    public User(Long id, String name, Integer age, String remark, String sex, String enabledFlag, String createDate, String updateDate) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.remark = remark;
        this.sex = sex;
        this.enabledFlag = enabledFlag;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
}

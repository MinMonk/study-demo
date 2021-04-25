package com.monk.app.bean;

import com.fasterxml.jackson.annotation.JsonView;

public class User {

    public interface UserSimpleView {};
    public interface UserDetailView extends UserSimpleView {};

    @JsonView(UserSimpleView.class)
    private Long id;

    @JsonView(UserSimpleView.class)
    private String userName;

    @JsonView(UserDetailView.class)
    private String password;

    @JsonView(UserSimpleView.class)
    private int age;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

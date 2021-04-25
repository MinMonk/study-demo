package com.monk.app.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;

class User implements Serializable{

    private static final long serialVersionUID = -923993135602126633L;

    private Long userId;

    private String userName;

    private BigDecimal salary;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", salary=" + salary +
                '}';
    }
}

public class SerializableTest {

    public static final Logger logger = LoggerFactory.getLogger(SerializableTest.class);

    @Test
    public void writeFile() {
        User user = new User();
        user.setUserId(1L);
        user.setUserName("zhangsan");
        user.setSalary(BigDecimal.valueOf(100L));
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(new File("D:\\testDir\\SerializableTest.txt")));
            outputStream.writeObject(user);
            outputStream.close();
        }catch (IOException ex){
            logger.error("写文件失败", ex);
        }
    }

    @Test
    public void readFile(){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File("D:\\testDir\\SerializableTest.txt")));
            User user = (User) inputStream.readObject();
            System.out.println(user.toString());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}

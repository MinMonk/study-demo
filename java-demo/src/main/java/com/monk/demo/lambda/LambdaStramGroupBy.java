/**
 * 
 * 文件名：LambdaStramGroupBy.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.demo.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.MapUtils;

class Studen {
    private int id;
    private String name;
    private String classNo;

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

    public String getClassNo() {
        return classNo;
    }

    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }

    @Override
    public String toString() {
        return "Studen [id=" + id + ", name=" + name + ", classNo=" + classNo + "]";
    }

    public List<Studen> initData(int num) {
        List<Studen> data = new ArrayList<Studen>();
        int n = 0;
        for (int i = 1; i <= num; i++) {
            Studen stu = new Studen();
            stu.setId(i);
            stu.setName("tom-" + i);
            if (i % 2 == 0) {
                n++;
            }
            stu.setClassNo("C00" + n);
            data.add(stu);
        }
        return data;
    }
}

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年1月19日 下午2:48:24
 */
public class LambdaStramGroupBy {

    public static void main(String[] args) {
        List<Studen> data = new Studen().initData(0);
        data.forEach(System.out::println);
        System.out.println("====================");
        Map<String, List<Studen>> map = data.stream().collect(Collectors.groupingBy(Studen::getClassNo));
        System.out.println(MapUtils.getObject(map, "C001").toString());
    }

}

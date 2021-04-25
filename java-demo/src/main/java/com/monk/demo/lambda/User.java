/**
 * 文件名：User.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.demo.lambda;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * @author Monk
 * @version V1.0
 * @date 2020年12月28日 上午10:08:03
 */
public class User{
    private Long id;
    private String name;

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

    public User() {
        id = 1L;
        name = "jack";
    }

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static User createUser(Supplier<User> supplier) {
        return supplier.get();
    }

    public static void main(String[] args) {
        User u1 = createUser(User::new);
        User u2 = createUser(User::new);
        System.out.println(u1.equals(u2));
        System.out.println("======================");
        Comparator<User> comparator = new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                int result = o1.getId().compareTo(o2.getId());
                return result;
            }
        };

        Predicate<User> predicate = new Predicate<User>() {
            @Override
            public boolean test(User user) {
                return user.getId() >= 0;
            }

        };

        List<User> userList = Lists.newArrayList(new User(1L, "tom"), new User(1L, "jack"), new User(2L, "tom"));


        User minUser = userList.stream().min(comparator::compare).get();
        System.out.println(minUser);
        System.out.println("======================");
        userList.stream().map(User::getId).forEach(System.out::println);
        System.out.println("======================");
        List<User> userList1 = userList.stream().filter(predicate::test).collect(Collectors.toList());
        userList.stream().filter(predicate::test).forEach(System.out::println);
        System.out.println("===========filter after===========");
        userList.forEach(System.out::println);
        System.out.println("======================");
        userList.stream().sorted(comparator::compare).forEach(System.out::println);
        System.out.println("======================");
        userList.stream().filter(distinctByKey(o -> o.getId())).forEach(System.out::println);
        System.out.println("=========1111111=============");
        userList.stream().collect(collectingAndThen(toCollection(()->new TreeSet<>(Comparator.comparing(o -> o.getId()))),ArrayList::new)).forEach(System.out::println);
        System.out.println("=========1111111=============");
        userList.stream().collect(
                collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getId()))),
                        ArrayList::new)).forEach(System.out::println);
        System.out.println("==========sorted============");
        userList.stream().filter(distinctByKey(o -> o.getId())).sorted(Comparator.comparing(User::getId).reversed()).forEach(System.out::println);
        System.out.println("==========to map============");
        userList.stream().collect(Collectors.toMap(User::getId, User::getName,
                (oldValue, newValue) -> newValue)).forEach((key, value) -> System.out.println(key + "=" + value));
        userList.stream().collect(Collectors.toMap(User::getId, User::getName,
                (oldValue, newValue) -> newValue)).forEach((key, value) -> System.out.println(key + "=" + value));

        List<Integer> integerList = Lists.newArrayList(-3, 5, -2, 9, -1, 1);
        integerList.stream().map(Math::abs).map(n -> n * 2).forEach(System.out::println);
        System.out.println("==========list============");
        System.out.println(integerList.stream().map(Math::abs).allMatch(e -> e > 0));
        System.out.println(integerList.stream().map(Math::abs).distinct().sorted(Comparator.reverseOrder()).collect(Collectors.toList()));

        String[] array = {"a", "b", "c"};
        for(Integer i : Lists.newArrayList(1,2,3)){
            Stream.of(array).map(item -> Strings.padEnd(item, i, '@')).forEach(System.out::println);
        }

    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Long> extractor) {
        Map<Object, Boolean> distinctMap = new ConcurrentHashMap<Object, Boolean>();
        return t -> distinctMap.put(extractor.apply(t), Boolean.TRUE) == null;
    }

}

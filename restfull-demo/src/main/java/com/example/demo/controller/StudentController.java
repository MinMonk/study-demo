package com.example.demo.controller;

import com.example.demo.entity.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class StudentController {

    @GetMapping
    @ApiOperation(value = "获取学生信息")
    public User getUser(@RequestParam(required = false) String name){
        User user = new User();
        user.setId(1L);
        user.setName(name);
        user.setAge(18);
        user.setRemark("纯URI参数");
        return user;
    }
}

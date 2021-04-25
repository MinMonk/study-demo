package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.SysUserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
@Api(value = "/user", description = "Operations about user")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private SysUserRepository userRepository;

    @GetMapping
    @ApiOperation(value = "查询全部用户信息")
    public Map<String, Object> queryAllUser(User user, @RequestParam(required = false) Integer num, @RequestParam(required = false) Boolean localFlag) {
        Map<String, Object> result = new HashMap<>();
        List<User> users = new ArrayList<User>();
        try {
            localFlag = null == localFlag ? true : localFlag;
            if (localFlag) {
                users = getLocalUsers(num);
            } else {
                users = userRepository.queryAllUser(user);
            }
            result.put("code", "200");
            result.put("msg", "查询成功");
            result.put("result", users);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.put("code", "500");
            result.put("msg", "查询失败" + e.getMessage());
        }

        return result;
    }

    private List<User> getLocalUsers(@RequestParam(required = false) Integer num) {
        num = null == num ? 19 : num;
        //logger.info("for loop num:[{}]", num);
        List<User> users = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            User temp = new User();
            temp.setId(Long.valueOf(i));
            temp.setName("test_user_" + i);
            temp.setSex(i % 2 == 0 ? "man" : "women");
            temp.setAge(18);
            temp.setRemark("测试用户" + i);
            temp.setEnabledFlag("Y");
            temp.setCreateDate(new Date().toString());
            temp.setUpdateDate(new Date().toString());
            users.add(temp);
        }
        return users;
    }


    @GetMapping("/{id:\\d+}/{sex}")
    @ApiOperation(value = "查询全部用户信息")
    public Map<String, Object> queryAllUser(@PathVariable Long id, @PathVariable String sex) {
        Map<String, Object> result = new HashMap<>();
        try {
            User user = new User();
            user.setId(id);
            user.setSex(sex);
            List<User> users = userRepository.queryAllUser(user);
            result.put("code", "200");
            result.put("msg", "查询成功");
            result.put("result", users);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.put("code", "500");
            result.put("msg", "查询失败" + e.getMessage());
        }

        return result;
    }

    @GetMapping("/{id:\\d+}")
    @ApiOperation(value = "查询指定用户信息")
    public Map<String, Object> getUser(@PathVariable Long id, @RequestParam(required = false) String name, @RequestBody(required = false) String body) {
        Map<String, Object> result = new HashMap<>();
        logger.info("get request body:[{}]", body);
        try {
            User user = userRepository.getUser(id);
            if (null == user) {
                result.put("code", "404");
                result.put("msg", "用户不存在");
            } else {
                user.setRemark("URI参数：" + name);
                result.put("code", "200");
                result.put("msg", "查询成功");
                result.put("result", user);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.put("code", "500");
            result.put("msg", "查询失败" + e.getMessage());
        }

        return result;
    }

    @PostMapping
    @ApiOperation(value = "创建用户")
    public Map<String, Object> createUser(@RequestBody User user, @RequestParam(required = false) String name) {
        Map<String, Object> result = new HashMap<>();
        try {
            userRepository.insertUser(user);
            result.put("code", "201");
            result.put("msg", "创建成功");
            result.put("URI parameter : name = ", name);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.put("code", "500");
            result.put("msg", "创建失败" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/{id:\\d+}")
    @ApiOperation(value = "更新用户信息")
    public Map<String, Object> updateUser(@PathVariable Long id, @RequestBody User user, @RequestParam(required = false) String name) {
        Map<String, Object> result = new HashMap<>();
        try {
            user.setId(id);
            userRepository.updateUser(user);
            User temp = userRepository.getUser(user.getId());
            result.put("code", "201");
            result.put("msg", "更新成功");
            result.put("result", temp);
            result.put("URI parameter : name = ", name);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.put("code", "500");
            result.put("msg", "更新失败" + e.getMessage());
        }

        return result;
    }

    @DeleteMapping("/{id:\\d+}")
    @ApiOperation(value = "删除用户信息")
    public Map<String, Object> deleteUser(@PathVariable Long id, @RequestParam(required = false) String name) {
        Map<String, Object> result = new HashMap<>();
        try {
            userRepository.deleteUser(id);
            result.put("code", "200");
            result.put("msg", "删除成功");
            result.put("URI parameter : name = ", name);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.put("code", "500");
            result.put("msg", "删除失败" + e.getMessage());
        }
        return result;
    }
}

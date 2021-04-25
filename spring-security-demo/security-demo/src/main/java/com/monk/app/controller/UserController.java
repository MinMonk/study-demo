package com.monk.app.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.monk.app.bean.User;
import com.monk.app.exception.CustomUserNotExistException;
import com.monk.app.propertites.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Monk
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    @Autowired
    private SecurityProperties securityProperties;

    @PostMapping("/register")
    public void registerUser(User user, HttpServletRequest request){
        String userName = user.getUserName();
        providerSignInUtils.doPostSignUp(userName, new ServletWebRequest(request));

    }

    @GetMapping("/me")
    public Object getCurrUserInfo(Authentication authentication, HttpServletRequest request) throws Exception{

        String authorization = request.getHeader("Authorization");
        String token = StringUtils.substringAfter(authorization, "Bearer ");

        Claims claims = Jwts.parser().setSigningKey(securityProperties.getOauth2().getJwtSignKey().getBytes("UTF-8"))
                .parseClaimsJws(token).getBody();

        String enhancerBy = (String) claims.get("enhancer by");
        logger.info("enhanceBy-->{}", enhancerBy);

        return authentication;
    }

    @GetMapping("me1")
    public Object me1(Authentication authentication){
        return authentication;
    }

    @GetMapping("me2")
    public Object me2(@AuthenticationPrincipal UserDetails userDetails){
        return userDetails;
    }

    @GetMapping
    @JsonView(User.UserSimpleView.class)
    public List<User> queryAllUser(User user) {

        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        users.add(new User());
        logger.info("查询全部用户信息，查询到的结果集数量为:{}", users.size());
        return users;
    }

    @GetMapping("/{id:\\d+}")
    @JsonView(User.UserDetailView.class)
    public User getUser(@PathVariable Long id) {
        Long userId = Long.valueOf(id);
        if (userId > 100 && userId < 200) {
            throw new CustomUserNotExistException(1L);
        } else if(userId >= 200){
            throw new NullPointerException("-------");
        }else {
            User user = new User();
            user.setId(1L);
            user.setUserName("jack");
            user.setAge(18);
            logger.info(ReflectionToStringBuilder.toString(user, ToStringStyle.MULTI_LINE_STYLE));
            return user;
        }
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        logger.info("创建的用户信息为：{}", user);
        user.setId(1L);
        return user;
    }

    @PutMapping("/{id:\\d+}")
    public void updateUser(@RequestBody User user){
        logger.info("更新后的用户信息为：{}", user);
    }

    @DeleteMapping("/{id:\\d+}")
    public void deleteUser(@PathVariable Long id){
        logger.info("删除了ID为{}的用户", id);
    }
}

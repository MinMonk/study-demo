package com.monk.app.common;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

/**
 * @ClassName CustomConnectionSignUp
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/4
 * @Version V1.0
 **/
@Component
public class CustomConnectionSignUp implements ConnectionSignUp {
    @Override
    public String execute(Connection<?> connection) {

        // 根据社交用户信息默认创建用户并返回用户唯一标识
        return connection.getDisplayName();
    }
}

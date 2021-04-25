package com.monk.app.exception;

/**
 * @ClassName UserNotExistException
 * @Description: TODO
 * @Author Monk
 * @Date 2020/4/5
 * @Version V1.0
 **/
public class CustomUserNotExistException extends RuntimeException {

    private Long userId;

    public CustomUserNotExistException(Long userid){
        super("user is not exist");
        this.userId = userid;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

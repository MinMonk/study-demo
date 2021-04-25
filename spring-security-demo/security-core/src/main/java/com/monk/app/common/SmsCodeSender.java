package com.monk.app.common;

/**
 * @author Monk
 */
public interface SmsCodeSender {

    /**
     * 功能描述:
     * 〈手机验证码发送功能〉
     * @Param: [手机号码, 验证码]
     * @Return: void
     * @Author: Monk
     * @Date: 2020/4/12 11:08
     */
    void send(String mobile, String code);
}

package com.monk.app.common.impl;

import com.monk.app.common.SmsCodeSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName DefaultSmsCodeSender
 * @Description: TODO
 * @Author Monk
 * @Date 2020/4/12
 * @Version V1.0
 **/
public class DefaultSmsCodeSender implements SmsCodeSender {

    /**
     * 日志
     */
    private final static Logger logger = LoggerFactory.getLogger(DefaultSmsCodeSender.class);

    @Override
    public void send(String mobile, String code) {
        logger.info("向手机号码{}发送了验证码{}", mobile, code);
    }
}

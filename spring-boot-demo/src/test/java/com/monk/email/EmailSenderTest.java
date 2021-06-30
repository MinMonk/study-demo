/**
 * 
 * 文件名：EmailSenderTest.java
 * 版权： Copyright 2017-2022 CMCC All Rights Reserved.
 * 描述： ESB管理系统
 */
package com.monk.email;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import com.monk.common.constant.Constants;
import com.monk.common.email.EmailSenderFactory;
import com.monk.common.email.IMultipartEmailSender;
import com.monk.common.email.MessageType;

import lombok.extern.slf4j.Slf4j;

/**
 * 邮件发送单元测试类
 * @author Monk
 * @version V1.0
 * @date 2021年6月30日 上午11:15:06
 */
@SpringBootTest
@Slf4j
public class EmailSenderTest {

    @Test
    void sendEmail() {
        IMultipartEmailSender emailSender = EmailSenderFactory.getEmailSender(MessageType.FLOW_MESSAGE);
        String currDateStr = DateFormatUtils.format(new Date(), DateFormatUtils.ISO_DATETIME_FORMAT.getPattern());
        Map<String, String> result = emailSender.sendEmail("测试邮件",
                "hello monk, 这是在打包构建的时候发送的一封测试邮件。当前时间: " + currDateStr, "wangliang@vispractice.com", null, null);
        String flag = result.get(IMultipartEmailSender.SEND_FLAG);
        String msg = result.get(IMultipartEmailSender.SEND_MSG);
        log.info("send email test result. flag=[{}], msg=[{}]", flag, msg);
        assertEquals(Constants.ENABLED_FLAG_Y, flag);
    }

}

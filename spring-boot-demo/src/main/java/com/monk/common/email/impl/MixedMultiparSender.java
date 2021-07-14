package com.monk.common.email.impl;

import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

/**
 * Multipar --> mixed类型的邮件发送(可以包含附件)
 * 
 * @author Monk
 * @version V1.0
 * @date 2021年6月8日 下午5:33:52
 */
@Service("mixedMultiparSender")
public class MixedMultiparSender extends AbstractMultiparSender {

    @Override
    protected MimeMessage buildContent(MimeMessage message, String content, Map<String, Object> extAttributes)
            throws Exception {

        message.setContent(content, "text/html;charset=UTF-8");

        return message;
    }
}

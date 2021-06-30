/**
 * 
 * 文件名：JMSListener.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.demo.jms.topic;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2019年1月8日 下午5:37:17
 */
public class JMSListener implements MessageListener {
    
    private static Logger logger = LoggerFactory.getLogger(JMSListener.class);

    public void onMessage(Message message) {
        try {
            logger.info("收到的消息：" + ((TextMessage)message).getText());
        } catch (JMSException e) {
            logger.error("监听消息失败：" + e.getMessage(), e);
        }
        
    }

}

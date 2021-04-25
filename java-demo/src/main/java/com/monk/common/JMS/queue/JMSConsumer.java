/**
 * 
 * 文件名：JMSConsumer.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.common.JMS.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息消费者
 * @author Monk
 * @version V1.0
 * @date 2019年1月8日 下午4:40:21
 */
public class JMSConsumer {
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    private static final String BROKERURL = ActiveMQConnection.DEFAULT_BROKER_URL;
    
    /** 连接工厂 */
    private static ConnectionFactory connFactory = null;
    
    /** jms连接对象*/
    private static Connection conn = null;
    
    /** 会话连接*/
    private static Session session = null;
    
    /** 消息目的地*/
    private static Destination destination = null;
    
    /** 消息消费者*/
    private static MessageConsumer messageConsumer = null;
    
    /** 日志 */
    private static Logger logger = LoggerFactory.getLogger(JMSConsumer.class);
    
    public static void main(String[] args) {

        connFactory = new ActiveMQConnectionFactory(JMSConsumer.USERNAME, JMSConsumer.PASSWORD, JMSConsumer.BROKERURL);
        try {
            //1. 创建连接工厂
            conn = connFactory.createConnection();
            
            //2. 启动连接
            conn.start();
            
            //3. 获取session(参数1： true:启用事物，false：不启用事物；  参数2：消息的确认方式 
            session = conn.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            
            //4. 创建消息队列
            destination = session.createQueue("FirstQueue");
            
            //5.创建消息消费者
            messageConsumer = session.createConsumer(destination);
            
            //6. 消费消息 方式一：
           /* while(true) {
                TextMessage message = (TextMessage) messageConsumer.receive(10000);
                if(message != null) {
                    logger.info("收到的消息：" + message.getText());
                }else { 
                    break;
                }
            }*/
            
            //6. 消费消息  方式二：（监听方式）
            messageConsumer.setMessageListener(new JMSListener());
            
        } catch (JMSException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if(session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                   logger.error("close jsm session failed. " + e.getMessage(), e);
                }
            }
            if(conn != null) {
                try {
                    conn.close();
                } catch (JMSException e) {
                    logger.error("close jsm connection failed. " + e.getMessage(), e);
                }
            }
        }
    
    }
}

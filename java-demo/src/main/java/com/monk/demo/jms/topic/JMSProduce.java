/**
 * 
 * 文件名：JMSProduce.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.demo.jms.topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息生产者
 * @author Monk
 * @version V1.0
 * @date 2019年1月8日 下午3:58:54
 */
public class JMSProduce {
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    private static final String BROKERURL = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static final int SENDNUM = 10;
    
    /** 连接工厂 */
    private static ConnectionFactory connFactory = null;
    
    /** jms连接对象*/
    private static Connection conn = null;
    
    /** 会话连接*/
    private static Session session = null;
    
    /** 消息目的地*/
    private static Destination destination = null;
    
    /** 消息生产者*/
    private static MessageProducer messageProducer = null;
    
    /** 日志 */
    private static Logger logger = LoggerFactory.getLogger(JMSProduce.class);
    
    public static void main(String[] args) {
        connFactory = new ActiveMQConnectionFactory(JMSProduce.USERNAME, JMSProduce.PASSWORD, JMSProduce.BROKERURL);
        try {
            //1. 创建连接工厂
            conn = connFactory.createConnection();
            
            //2. 启动连接
            conn.start();
            
            //3. 获取session(参数1： true:启用事物，false：不启用事物；  参数2：消息的确认方式 
            session = conn.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            
            //4. 创建消息队列
            destination = session.createTopic("FirstQueue");
            
            //5.创建消息生产者
            messageProducer = session.createProducer(destination);
            
            //6. 发送消息
            sendMessage(session, messageProducer);
            session.commit();
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
    
    /**
     * 发送消息
     * @param session 连接对象
     * @param messageProducer 消息生产者
     * @throws JMSException 
     * @author Monk
     * @date 2019年1月8日 下午4:26:13
     */
    private static void sendMessage(Session session, MessageProducer messageProducer) throws JMSException {
        for (int i = 0; i < JMSProduce.SENDNUM; i++) {
            TextMessage message = session.createTextMessage("Active MQ 发送的消息" + i);
            messageProducer.send(message);
            logger.info("发送消息成功：" + "Active MQ 发送的消息" + i);
        }
    }
}

/**
 * 
 * 文件名：SendLog.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.jms;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年2月2日 下午7:03:26
 */
public class SendLog {
    
    private static final Logger logger = LoggerFactory.getLogger(SendLog.class);
    
    private static final String KEY_PROVIDER_URL = "jms.queue.provider_url";
    private static final String KEY_USERNAME = "jms.queue.username";
    private static final String KEY_PASSWORD = "jms.queue.password";
    private static final String KEY_FACTORY_JNDI = "jms.queue.factory_jndi";
    private static final String KEY_QUEUE_JNDI = "jms.queue.queue_jndi";

    private QueueSender sender = null;
    private QueueSession session = null;
    private static final String DEFAULT_JMS_FACTORY_JNDI = "ConnectionFactory-wang";
    private static final String DEFAULT_JMS_QUEUE_JNDI = "Queue-test";
    
    private String factoryJndi;
    private String queueJndi;

    public void sendMessage(String msg) {
        TextMessage textMsg;
        try {
            if (this.sender == null) {
                this.init();
            }
            textMsg = session.createTextMessage();
            textMsg.setText(msg);
            sender.send(textMsg);
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 1. 连接jms服务器
    // 2. 获取连接工厂(Connection Factory)
    // 3. 通过连接工厂创建队列连接（QueueConnection)
    // 4. 通过队列连接创建队列会话（QueueSession)
    // 5. 通过队列会话创建队列生产者（Sender/Product)
    // 6. 创建消息(Message)
    // 7. 通过生产者将消息发送到队列中
    private void init() throws NamingException, JMSException {
        Hashtable properties = new Hashtable();
        
        Properties pro = resolveProperties();
        this.factoryJndi = getValueFromProperties(KEY_FACTORY_JNDI, pro);
        this.queueJndi = getValueFromProperties(KEY_QUEUE_JNDI, pro);
        
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        properties.put(Context.PROVIDER_URL, pro.get(KEY_PROVIDER_URL));
        properties.put(Context.SECURITY_PRINCIPAL, pro.get(KEY_USERNAME));
        properties.put(Context.SECURITY_CREDENTIALS, pro.get(KEY_PASSWORD));
        logger.info("jms configuration:[{}]", properties);
        logger.info("factoryJndi:[{}], queueJndi:[{}]", factoryJndi, queueJndi);
        InitialContext ctx = new InitialContext(properties);
        QueueConnectionFactory jmsFactory = (QueueConnectionFactory) ctx.lookup(factoryJndi);
        QueueConnection jmsConn = jmsFactory.createQueueConnection();
        session = jmsConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = (Queue) ctx.lookup(queueJndi);
        sender = session.createSender(queue);
    }

    public static void main(String[] cmd) {
        SendLog sender = new SendLog();
        sender.sendMessage("hello world");
    }
    
    private static String getValueFromProperties(String key, Properties pro) throws JMSException {
        Object obj = pro.get(key);
        if(null == obj) {
            if(key.equals(KEY_FACTORY_JNDI)) {
                logger.info("the default value will be used. key:{}, value:{}", key, DEFAULT_JMS_FACTORY_JNDI);
                return DEFAULT_JMS_FACTORY_JNDI;
            }else if(key.equals(KEY_QUEUE_JNDI)) {
                logger.info("the default value will be used. key:{}, value:{}", key, DEFAULT_JMS_FACTORY_JNDI);
                return DEFAULT_JMS_QUEUE_JNDI;
            }else {
                throw new JMSException("unkonw value by key" + key);
            }
        }else {
            return obj.toString();
        }
    }
    
    private static Properties resolveProperties() {
        Properties properties = new Properties();
        InputStream in = JmsProducer.class.getClassLoader().getResourceAsStream("jms.properties");
        try {
            properties.load(in);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);    
        }
        return properties;
    }

}

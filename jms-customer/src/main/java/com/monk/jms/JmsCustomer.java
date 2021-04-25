/**
 * 
 * 文件名：JmsCustomer.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.jms;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年2月2日 下午3:00:45
 */
public class JmsCustomer {

    private static final Logger logger = LoggerFactory.getLogger(JmsCustomer.class);
    
    private static final String KEY_PROVIDER_URL = "jms.topic.provider_url";
    private static final String KEY_USERNAME = "jms.topic.username";
    private static final String KEY_PASSWORD = "jms.topic.password";
    private static final String KEY_FACTORY_JNDI = "jms.topic.factory_jndi";
    private static final String KEY_TOPIC_JNDI = "jms.topic.topic_jndi";
    private static final String KEY_CLIENT_ID = "jms.topic.client_id";
    

    private static final String DEFAULT_JMS_FACTORY_JNDI = "ConnectionFactory-wang";
    private static final String DEFAULT_JMS_TOPIC_JNDI = "Topic-Vendor";
    private static final String DEFAULT_JMS_CLIENT_ID = "SUB_EIP";

    private TopicSubscriber reciver = null;
    private String cliendId;
    private String factoryJndi;
    private String topicJndi;

    public void reciveMessage() {
        try {
            if (this.reciver == null) {
                this.init();
            }
            logger.info("waiting to recive message from jms topic [{}], currend client id [{}]", topicJndi, cliendId);
            while (true) {
                Message msg = reciver.receive();
                msg.getJMSDestination();
                if (msg instanceof TextMessage) {
                    TextMessage textMsg = (TextMessage) msg;
                    logger.info("recive jms message:{}", textMsg.getText());
                }
            }
        } catch (JMSException e) {
            logger.error(e.getMessage(), e);    
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);    
        }
    }

    private void init() throws NamingException, JMSException {
        Hashtable properties = new Hashtable();
        
        Properties pro = resolveProperties();
        this.factoryJndi = getValueFromProperties(KEY_FACTORY_JNDI, pro);
        if(null == topicJndi) {
            this.topicJndi = getValueFromProperties(KEY_TOPIC_JNDI, pro);
        }
        if(null == cliendId) {
            this.cliendId = getValueFromProperties(KEY_CLIENT_ID, pro);
        }
        
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        properties.put(Context.PROVIDER_URL, pro.get(KEY_PROVIDER_URL));
        properties.put(Context.SECURITY_PRINCIPAL, pro.get(KEY_USERNAME));
        properties.put(Context.SECURITY_CREDENTIALS, pro.get(KEY_PASSWORD));
        logger.info("jms configuration:[{}]", properties);
        InitialContext ctx = new InitialContext(properties);
        TopicConnectionFactory jmsFactory = (TopicConnectionFactory) ctx.lookup(factoryJndi);
        TopicConnection jmsConn = jmsFactory.createTopicConnection();
        jmsConn.setClientID(cliendId);
        TopicSession session = jmsConn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = (Topic) ctx.lookup(topicJndi);
        // 创建持久性订阅
        reciver = session.createDurableSubscriber(topic, cliendId);
        
        // 创建非持久性订阅
        // reciver = session.createSubscriber(topic);
        jmsConn.start();
    }
    
    private static String getValueFromProperties(String key, Properties pro) throws JMSException {
        Object obj = pro.get(key);
        if(null == obj) {
            if(key.equals(KEY_FACTORY_JNDI)) {
                logger.info("the default value will be used. key:{}, value:{}", key, DEFAULT_JMS_FACTORY_JNDI);
                return DEFAULT_JMS_FACTORY_JNDI;
            }else if(key.equals(KEY_TOPIC_JNDI)) {
                logger.info("the default value will be used. key:{}, value:{}", key, DEFAULT_JMS_FACTORY_JNDI);
                return DEFAULT_JMS_TOPIC_JNDI;
            }else if(key.equals(KEY_CLIENT_ID)) {
                logger.info("the default value will be used. key:{}, value:{}", key, DEFAULT_JMS_FACTORY_JNDI);
                return DEFAULT_JMS_CLIENT_ID;
            }else {
                throw new JMSException("unkonw value by key" + key);
            }
        }else {
            return obj.toString();
        }
    }

    public static void main(String[] args) {
        JmsCustomer consumer = new JmsCustomer();
        if(null != args) {
            if(args.length >= 1) {
                consumer.setCliendId(args[0]);
            }
            if(args.length >= 2) {
                consumer.setTopicJndi(args[1]);
            }
        }
        consumer.reciveMessage();
    }
    
    
    public String getCliendId() {
        return cliendId;
    }

    public void setCliendId(String cliendId) {
        this.cliendId = cliendId;
    }

    public String getTopicJndi() {
        return topicJndi;
    }

    public void setTopicJndi(String topicJndi) {
        this.topicJndi = topicJndi;
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

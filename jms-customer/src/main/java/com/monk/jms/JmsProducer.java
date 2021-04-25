package com.monk.jms;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JmsProducer {
    private TopicPublisher sender = null;
    private TopicSession session = null;
    private static final String JMS_FACTORY_JNDI = "ConnectionFactory-wang";
    private static final String JMS_TOPIC_JNDI = "Topic-Vendor_AH";

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
    // 3. 通过连接工厂创建主题连接（TopicConnection)
    // 4. 通过主题连接创建主题会话（TopicSession)
    // 5. 通过主题会话创建主题发布者（Publisher)
    // 6. 创建消息(Message)
    // 7. 通过发布者将消息发送到主题中
    private void init() throws NamingException, JMSException {
        Hashtable properties = new Hashtable();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        properties.put(Context.PROVIDER_URL, "t3://10.204.105.128:8011");
        properties.put(Context.SECURITY_PRINCIPAL, "weblogic");
        properties.put(Context.SECURITY_CREDENTIALS, "weblogic123");
        InitialContext ctx = new InitialContext(properties);
        TopicConnectionFactory jmsFactory = (TopicConnectionFactory) ctx.lookup(JMS_FACTORY_JNDI);
        TopicConnection jmsConn = jmsFactory.createTopicConnection();
        session = jmsConn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = (Topic) ctx.lookup(JMS_TOPIC_JNDI);
        sender = session.createPublisher(topic);
    }

    public static void main(String[] cmd) {
        JmsProducer sender = new JmsProducer();
        sender.sendMessage("hello jms topic");
    }
    
}
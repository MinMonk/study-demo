package com.monk.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class TestProducer {

    public static void main(String[] args) {
        try {
            testQueueProducer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testQueueProducer() throws Exception {
        // 建立连接工厂，单例
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "admin", "tcp://127.0.0.1:61616");
        // 建立连接
        Connection connection = connectionFactory.createConnection();
        // 连接开启
        connection.start();
        // 创建一个服务对象session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 创建一个目的地Destination，,即ActiveMQ的接收点
        Queue queue = session.createQueue("csb.cluster.event");
        // 创建一个生产者，并将目的地告诉他
        MessageProducer producer = session.createProducer(queue);
        // 创建一个消息
        Message message = session.createTextMessage("hello queue message2");
        // 生产者发送消息
        producer.send(message);
        // 关闭连接
        producer.close();
        session.close();
        connection.close();
    }

}

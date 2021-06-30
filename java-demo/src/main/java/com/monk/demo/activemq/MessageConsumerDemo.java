package com.monk.demo.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class MessageConsumerDemo {
    
    public static void main(String[] args) throws Exception {
        testQueueCustomer();
    }

    public static void testQueueCustomer() throws Exception {
        // 建立连接工厂，单例
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "admin", "tcp://127.0.0.1:61616");
        // 建立连接
        Connection connection = connectionFactory.createConnection();
        // 连接开启
        connection.start();
        // 创建一个服务对象session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 创建一个目的地Destination,即ActiveMQ的接收点
        Queue queue = session.createQueue("csb.cluster.event");
        // 创建一个消费者，并将目的地告诉他
        MessageConsumer consumer = session.createConsumer(queue);
        // 消费者实时监听是否有消息传来，传来后打印出来
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("consumer : " + textMessage.getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        // 手动关闭实时等待
        System.in.read();
        // 关闭连接
        consumer.close();
        session.close();
        connection.close();
    }
}

/**
 * 
 * 文件名：SendEmailDemo.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.common.email;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 使用javaxMail发送邮件Demo
 * 
 * @author Monk
 * @version V1.0
 * @date 2019年5月22日 下午5:39:26
 */
public class SendEmailByJavaxMailDemo {

    public static void main(String[] args) {

        List<String> userList = new ArrayList<String>();
        userList.add("zhangsan@163.com");
        userList.add("lisi@163.com");
        userList.add("wangwu@163.com");

        String content = generateEmailContent();

        try {
            sendMail("服务告警通知", content, userList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构造邮件正文
     * 
     * @return 邮件正文
     * @author Monk
     * @date 2019年5月27日 下午4:05:20
     */
    private static String generateEmailContent() {
        StringBuilder content = new StringBuilder("<html><head></head><body>");
        content.append("<p>测试邮件----测试内容</p>");
        content.append("</body></html>");
        return content.toString();
    }

    /**
     * 发送邮件
     * 
     * @param subject
     *            邮件主题
     * @param content
     *            邮件内容（支持HTML）
     * @param toEmailAddres
     *            收件人
     * @throws Exception
     * @author Monk
     * @date 2019年5月22日 下午6:27:27
     */
    private static void sendMail(String subject, String content, List<String> userList) throws Exception {

        InternetAddress[] toEmailAddres = getToEmailUser(userList);

        String host = "smtp.163.com";
        String port = "25";
        String auth = "false";
        String protocol = "smtp";
        String mailFrom = "zhangsan@163.com";
        String personalName = "zhangsan";
        String username = "zhangsan@163.com";
        String password = "123456";
        String mailDebug = "false";
        String contentType = null;

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", auth == null ? "true" : auth);
        props.put("mail.transport.protocol", protocol == null ? "smtp" : protocol);
        props.put("mail.smtp.port", port == null ? "25" : port);
        props.put("mail.debug", mailDebug == null ? "false" : mailDebug);
        Session mailSession = Session.getInstance(props);

        // 设置session,和邮件服务器进行通讯。
        MimeMessage message = new MimeMessage(mailSession);
        // 设置邮件主题
        message.setSubject(subject);
        // 设置邮件正文
        message.setContent(content, contentType == null ? "text/html;charset=UTF-8" : contentType);
        // 设置邮件发送日期
        message.setSentDate(new Date());
        InternetAddress address = new InternetAddress(mailFrom, personalName);
        // 设置邮件发送者的地址
        message.setFrom(address);
        // 设置邮件接收方的地址
        message.setRecipients(Message.RecipientType.TO, toEmailAddres);
        Transport transport = null;
        transport = mailSession.getTransport();
        message.saveChanges();

        transport.connect(host, username, password);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    private static InternetAddress[] getToEmailUser(List<String> userList) {
        // List<InternetAddress> list = new ArrayList<InternetAddress>();
        InternetAddress[] list = null;
        try {
            Set<String> set = new HashSet<String>();
            for (int i = 0; i < userList.size(); i++) {
                set.add(userList.get(i));
            }
            list = new InternetAddress[set.size()];
            int i = 0;
            for (Iterator iterator = set.iterator(); iterator.hasNext();) {
                String string = (String) iterator.next();
                InternetAddress address = new InternetAddress(string);
                list[i] = address;
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < list.length; i++) {
            System.out.println(list[i]);
        }

        return list;
    }

}

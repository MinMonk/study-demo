/**
 * 
 * 文件名：AbstractEmailSender.java
 * 版权： Copyright 2017-2022 CMCC All Rights Reserved.
 * 描述： ESB管理系统
 */
package com.monk.common.email.impl;

import java.security.GeneralSecurityException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import com.monk.common.constant.Constants;
import com.monk.common.email.IMultipartEmailSender;
import com.monk.common.holder.SpringContextHolder;
import com.monk.common.utils.EncryptUtil;
import com.sun.mail.util.MailSSLSocketFactory;

/**
 * Multipar类型的邮件发送(可以包含附件)
 * 
 * @author Monk
 * @version V1.0
 * @date 2021年6月8日 上午11:36:37
 */
public abstract class AbstractMultiparSender implements IMultipartEmailSender {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(AbstractMultiparSender.class);

    /**
     * 邮箱服务器相关配置
     */
    private static final String PROPERTIES_KEY_HOST = "mail.smtp.host";
    private static final String PROPERTIES_KEY_PORT = "mail.smtp.port";
    private static final String PROPERTIES_KEY_AUTH = "mail.smtp.auth";
    private static final String PROPERTIES_KEY_USERNAME = "mail.username";
    private static final String PROPERTIES_KEY_PASSWORD = "mail.password";
    private static final String PROPERTIES_KEY_SSL_ENABLED = "mail.smtp.ssl.enable";
    private static final String PROPERTIES_KEY_SSL_SOCKETFACTORY = "mail.smtp.ssl.socketFactory";
    private static final String PROPERTIES_KEY_TRANSPORT_PROTOCOL = "mail.transport.protocol";
    private static final String PROPERTIES_KEY_MAIL_FROM = "mail.from";
    private static final String PROPERTIES_KEY_MAIL_FROMNAME = "mail.fromName";

    private static final String DEFAULT_PORT = "25";
    private static final String DEFAULT_SSL_PORT = "465";

    /**
     * 收件人邮件地址拆分字符
     */
    public static final String TO_ADDRESS_SPLIT_CHAR = "TO_SPLIT_CHAR";
    /**
     * 抄送人邮件地址拆分字符
     */
    public static final String CC_ADDRESS_SPLIT_CHAR = "CC_SPLIT_CHAR";

    /**
     * 默认的邮件地址拆分字符
     */
    public static final String DEFAULT_SPLIT_CHAR = ",";

    @Override
    public Map<String, String> sendEmail(String subject, String content, String toEmailAddresStr,
            String ccAddressStr, Map<String, Object> extAttributes) {
        String toSplitChar = MapUtils.getString(extAttributes, TO_ADDRESS_SPLIT_CHAR, DEFAULT_SPLIT_CHAR);
        String ccSplitChar = MapUtils.getString(extAttributes, CC_ADDRESS_SPLIT_CHAR, DEFAULT_SPLIT_CHAR);
        return this.sendEmail(subject, content, splitAddress(toEmailAddresStr, toSplitChar),
                splitAddress(ccAddressStr, ccSplitChar), extAttributes);
    }

    /**
     * 拆分邮件地址
     * 
     * @param addressStr
     *            邮件地址字符串
     * @param splitChar
     *            切割字符
     * @return 拆分后的邮件地址
     * @author Monk
     * @date 2021年6月8日 下午7:31:02
     */
    private List<String> splitAddress(String addressStr, String splitChar) {
        if (StringUtils.isNotBlank(addressStr)) {
            return Arrays.asList(addressStr.split(splitChar));
        }
        return null;
    }

    @Override
    public Map<String, String> sendEmail(String subject, String content, List<String> toEmailAddres,
            List<String> ccAddress, Map<String, Object> extAttributes) {
        long start = System.currentTimeMillis();
        Map<String, String> result = new HashMap<String, String>(2);

        Properties prop = new Properties();
        try {
            prop = loadEmailServerProperties();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.put(SEND_FLAG, Constants.ENABLED_FLAG_N);
            result.put(SEND_MSG, "加载邮箱服务器配置失败");
            return result;
        }

        logger.info("load email server properties success.");
        String host = prop.getProperty(PROPERTIES_KEY_HOST);
        String userName = prop.getProperty(PROPERTIES_KEY_USERNAME);
        String password = EncryptUtil.replacePrefixAndDecryptPwd(prop.getProperty(PROPERTIES_KEY_PASSWORD));
        // 获取默认session对象
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });

        if (CollectionUtils.isEmpty(toEmailAddres)) {
            result.put(SEND_FLAG, Constants.ENABLED_FLAG_N);
            result.put(SEND_MSG, "收件人地址不能为空");
            return result;
        }

        Transport transport = null;
        try {
            MimeMessage message = new MimeMessage(session);
            // 邮件主题,并指定编码格式
            message.setSubject(subject, "UTF-8");
            // 发件人
            String mailFrom = prop.getProperty(PROPERTIES_KEY_MAIL_FROM);
            String mailFromName = prop.getProperty(PROPERTIES_KEY_MAIL_FROMNAME);
            message.setFrom(new InternetAddress(mailFrom, mailFromName));
            // 设置主送人
            message.setRecipients(Message.RecipientType.TO, mergeAddres(toEmailAddres));
            // 设置抄送人
            if (CollectionUtils.isNotEmpty(ccAddress)) {
                message.setRecipients(Message.RecipientType.CC, mergeAddres(ccAddress));
            }
            message.setSentDate(new Date());

            buildContent(message, content, extAttributes);

            message.saveChanges();
            transport = session.getTransport();
            transport.connect(host, userName, password);
            transport.sendMessage(message, message.getAllRecipients());
            logger.info("{} send email success. csot {}ms", Thread.currentThread().getName(),
                    System.currentTimeMillis() - start);
            result.put(SEND_FLAG, Constants.ENABLED_FLAG_Y);
            result.put(SEND_MSG, "邮件发送成功");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.put(SEND_FLAG, Constants.ENABLED_FLAG_N);
            result.put(SEND_MSG, "邮件发送失败:" + e.getMessage());
            return result;
        } finally {
            if (null != transport) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        return result;
    }

    /**
     * 构建邮件正文内容
     * 
     * @param session
     *            邮箱服务器Session
     * @param content
     *            邮件内容
     * @param extAttributes
     *            扩展属性
     * @return 邮件正文
     * @author Monk
     * @throws Exception
     * @date 2021年6月8日 下午3:53:43
     */
    protected abstract MimeMessage buildContent(MimeMessage message, String content,
            Map<String, Object> extAttributes) throws Exception;

    /**
     * 合并邮箱地址
     * 
     * @param addressList
     *            邮箱地址
     * @return 合并后的邮箱地址
     * @throws AddressException
     * @author Monk
     * @date 2021年6月8日 下午4:16:32
     */
    private InternetAddress[] mergeAddres(List<String> addressList) throws AddressException {
        InternetAddress[] result = null;
        Set<String> set = new HashSet<String>(addressList);
        if (CollectionUtils.isNotEmpty(set)) {
            result = new InternetAddress[set.size()];
            int idx = 0;
            for (String addr : set) {
                result[idx] = new InternetAddress(addr);
                idx++;
            }
        }
        return result;
    }

    /**
     * 加载邮箱服务器配置信息
     * 
     * @return 邮箱服务器配置
     * @throws GeneralSecurityException
     *             通用安全异常
     * @author Monk
     * @date 2021年6月8日 下午3:48:11
     */
    private Properties loadEmailServerProperties() throws GeneralSecurityException {
        
        Environment env = SpringContextHolder.getApplicationContext().getEnvironment();
        String host = env.getProperty(PROPERTIES_KEY_HOST);
        String userName = env.getProperty(PROPERTIES_KEY_USERNAME);
        String password = env.getProperty(PROPERTIES_KEY_PASSWORD);
        String port = env.getProperty(PROPERTIES_KEY_PORT);
        String auth = env.getProperty(PROPERTIES_KEY_AUTH, "true");
        String sslEnabled = env.getProperty(PROPERTIES_KEY_SSL_ENABLED, "true");
        String transProtocol = env.getProperty(PROPERTIES_KEY_TRANSPORT_PROTOCOL, "smtp");
        String mailFrom = env.getProperty(PROPERTIES_KEY_MAIL_FROM, userName);
        String mailFromName = env.getProperty(PROPERTIES_KEY_MAIL_FROMNAME, userName);
        mailFrom = StringUtils.isBlank(mailFrom) ? userName : mailFrom;
        mailFromName = StringUtils.isBlank(mailFromName) ? userName : mailFromName;

        verifyRequireProp(PROPERTIES_KEY_HOST, host);
        verifyRequireProp(PROPERTIES_KEY_USERNAME, userName);
        verifyRequireProp(PROPERTIES_KEY_PASSWORD, password);

        Properties prop = new Properties();
        prop.setProperty(PROPERTIES_KEY_HOST, host);
        if (StringUtils.isBlank(port)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(sslEnabled)) {
                prop.setProperty(PROPERTIES_KEY_PORT, DEFAULT_SSL_PORT);
            } else {
                prop.setProperty(PROPERTIES_KEY_PORT, DEFAULT_PORT);
            }
        } else {
            prop.setProperty(PROPERTIES_KEY_PORT, port);
        }
        prop.setProperty(PROPERTIES_KEY_USERNAME, userName);
        prop.setProperty(PROPERTIES_KEY_PASSWORD, password);
        prop.setProperty(PROPERTIES_KEY_AUTH, auth);
        prop.setProperty(PROPERTIES_KEY_SSL_ENABLED, sslEnabled);
        if (Boolean.TRUE.toString().equalsIgnoreCase(sslEnabled)) {
            MailSSLSocketFactory sslfac = new MailSSLSocketFactory();
            sslfac.setTrustAllHosts(true);
            prop.put(PROPERTIES_KEY_SSL_SOCKETFACTORY, sslfac);
        }
        prop.setProperty(PROPERTIES_KEY_TRANSPORT_PROTOCOL, transProtocol);
        prop.setProperty(PROPERTIES_KEY_MAIL_FROM, mailFrom);
        prop.setProperty(PROPERTIES_KEY_MAIL_FROMNAME, mailFromName);

        return prop;
    }

    /**
     * 校验必填参数
     * 
     * @param propKey
     *            参数Key
     * @param propValue
     *            参数值
     * @author Monk
     * @date 2021年6月8日 下午3:42:51
     */
    private void verifyRequireProp(String propKey, String propValue) {
        if (StringUtils.isBlank(propValue)) {
            String errorMsg = MessageFormat
                    .format("Failed to load javax mail server config, the config key [{0}] is blank, please check the value.",
                            propKey);
            throw new RuntimeException(errorMsg);
        }
    }

}

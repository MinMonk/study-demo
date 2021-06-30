package com.monk.common.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monk.common.holder.SpringContextHolder;

/**
 * 发送邮箱工厂类
 * 
 * @author Monk
 * @version V1.0
 * @date 2021年6月9日 下午5:10:27
 */
public class EmailSenderFactory {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(EmailSenderFactory.class);

    /**
     * 根据消息类型获取对应的发送邮件实现类
     * 
     * @param msgType
     *            消息类型
     * @return 发送邮件实现类
     * @author Monk
     * @date 2021年6月9日 下午5:13:44
     */
    public static IMultipartEmailSender getEmailSender(MessageType msgType) {
        if (null == msgType) {
            logger.warn("Failed to get email sender instance. the msgType is blank.");
            return null;
        }

        switch (msgType) {
        case FLOW_MESSAGE:
            return SpringContextHolder.getBean("mixedMultiparSender");
        case REPORT_MESSAGE:
            return SpringContextHolder.getBean("relatedEmailSender");
        default:
            logger.warn("Failed to get email sender instance. unknown msgType. msgType:{}", msgType);
            return null;
        }
    }

}

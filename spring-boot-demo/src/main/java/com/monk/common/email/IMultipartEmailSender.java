package com.monk.common.email;

import java.util.List;
import java.util.Map;

/**
 * javaxEmail邮件发送接口定义类
 * 
 * @author Monk
 * @version V1.0
 * @date 2021年6月8日 下午5:24:18
 */
public interface IMultipartEmailSender {
    
    /**
     * 发送结果标识Key
     */
    String SEND_FLAG = "sendFlag";

    /**
     * 发送结果消息Key
     */
    String SEND_MSG = "sendMsg";

    /**
     * 发送邮件
     * 
     * @param subject
     *            邮件主题
     * @param content
     *            邮件正文
     * @param toEmailAddres
     *            收件人员
     * @param ccAddress
     *            抄送人员
     * @param extAttributes
     *            扩展属性字段
     * @return 邮件发送结果
     * @author Monk
     * @date 2021年6月8日 下午5:24:56
     */
    Map<String, String> sendEmail(String subject, String content, List<String> toEmailAddres,
            List<String> ccAddress, Map<String, Object> extAttributes);
    
    /**
     * 发送邮件
     * 
     * @param subject
     *            邮件主题
     * @param content
     *            邮件正文
     * @param toEmailAddresStr
     *            收件人员
     * @param ccAddressStr
     *            抄送人员
     * @param extAttributes
     *            扩展属性字段
     * @return 邮件发送结果
     * @author Monk
     * @date 2021年6月8日 下午7:22:42
     */
    Map<String, String> sendEmail(String subject, String content, String toEmailAddresStr,
            String ccAddressStr, Map<String, Object> extAttributes);
}

/**
 * 
 * 文件名：RelatedMultipartEmailSender.java
 * 版权： Copyright 2017-2022 CMCC All Rights Reserved.
 * 描述： ESB管理系统
 */
package com.monk.common.email.impl;

import java.io.File;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

/**
 * Multipart --> Related类型的邮件发送(可以包含内嵌资源)
 * 
 * @author Monk
 * @version V1.0
 * @date 2021年6月8日 下午5:03:38
 */
@Service("relatedEmailSender")
public class RelatedMultiparSender extends AbstractMultiparSender {

    @Override
    protected MimeMessage buildContent(MimeMessage message, String content, Map<String, Object> extAttributes)
            throws Exception {

        // 创建一个MIME子类型为“related”的MimeMultipart对象
        MimeMultipart mp = new MimeMultipart("related");
        // 创建一个表示正文的MimeBodyPart对象，并将它加入到前面创建的MimeMultipart对象中
        MimeBodyPart htmlPart = new MimeBodyPart();
        mp.addBodyPart(htmlPart);
        // 将MimeMultipart对象设置为整个邮件的内容
        message.setContent(mp);
        // 创建一个表示图片资源的MimeBodyPart对象，将将它加入到前面创建的MimeMultipart对象中
        MimeBodyPart imagePart = new MimeBodyPart();
        mp.addBodyPart(imagePart);
        // 设置内嵌图片邮件体
        String imagePath = MapUtils.getString(extAttributes, "imagePath");
        String imageName = MapUtils.getString(extAttributes, "imageName");
        DataSource ds = new FileDataSource(new File(imagePath));
        DataHandler dh = new DataHandler(ds);
        imagePart.setDataHandler(dh);
        // 设置内容编号,用于其它邮件体引用
        imagePart.setContentID(imageName);
        // 创建一个MIME子类型为"alternative"的MimeMultipart对象，并作为前面创建的htmlPart对象的邮件内容
        MimeMultipart htmlMultipart = new MimeMultipart("alternative");
        // 创建一个表示html正文的MimeBodyPart对象
        MimeBodyPart htmlBodypart = new MimeBodyPart();
        // 其中cid=androidlogo.gif是引用邮件内部的图片，即imagePart.setContentID("androidlogo.gif");方法所保存的图片
        htmlBodypart.setContent("<span style='color:#333;'>" + content + "<img src=\"cid:" + imageName + "\" />",
                "text/html;charset=utf-8");
        htmlMultipart.addBodyPart(htmlBodypart);
        htmlPart.setContent(htmlMultipart);

        return message;
    }

}

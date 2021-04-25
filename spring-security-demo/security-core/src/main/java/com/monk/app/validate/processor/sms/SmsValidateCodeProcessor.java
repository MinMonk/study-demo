package com.monk.app.validate.processor.sms;

import com.monk.app.validate.bean.ValidateCode;
import com.monk.app.validate.processor.impl.AbstractValidateCodeProcessor;
import com.monk.app.common.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @ClassName ImageCodeProcessor
 * @Description: TODO
 * @Author Monk
 * @Date 2020/4/12
 * @Version V1.0
 **/
@Component("smsValidateCodeProcessor")
public class SmsValidateCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

    @Autowired
    private SmsCodeSender smsCodeSender;

    @Override
    protected void send(ServletWebRequest request, ValidateCode imageCode) throws Exception {
        String mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), "mobile");
        smsCodeSender.send(mobile, imageCode.getCode());
    }
}

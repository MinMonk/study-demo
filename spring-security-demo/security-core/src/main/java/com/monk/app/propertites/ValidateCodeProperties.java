package com.monk.app.propertites;

/**
 * @ClassName ValidateCodeProperties
 * @Description: TODO
 * @Author Monk
 * @Date 2020/4/6
 * @Version V1.0
 **/
public class ValidateCodeProperties {

    /**
     * 图形验证码配置
     */
    private ImageCodeProperties imageCode = new ImageCodeProperties();

    /**
     * 短信验证码配置
     */
    private SmsCodeProperties smsCode = new SmsCodeProperties();

    public SmsCodeProperties getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(SmsCodeProperties smsCode) {
        this.smsCode = smsCode;
    }

    public ImageCodeProperties getImageCode() {
        return imageCode;
    }

    public void setImageCode(ImageCodeProperties imageCode) {
        this.imageCode = imageCode;
    }
}

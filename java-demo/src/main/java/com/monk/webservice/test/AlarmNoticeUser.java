/**
 * 文件名：AlarmNoticeUser.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.webservice.test;

/**
 * 告警通知用户信息类
 * 
 * @author huangyulan
 * @version V1.0
 * @date 2018年3月8日 上午9:28:01
 */
public class AlarmNoticeUser {

    private String policyCode;
    private String recevierType;
    private String groupCode;
    private Long userId;
    private String userAccount;
    private String userName;
    private String email;
    private String mobile;
    private String weixin;
    private String revMail;
    private String revSms;
    private String revWeixin;

    public String getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
    }

    public String getRecevierType() {
        return recevierType;
    }

    public void setRecevierType(String recevierType) {
        this.recevierType = recevierType;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getRevMail() {
        return revMail;
    }

    public void setRevMail(String revMail) {
        this.revMail = revMail;
    }

    public String getRevSms() {
        return revSms;
    }

    public void setRevSms(String revSms) {
        this.revSms = revSms;
    }

    public String getRevWeixin() {
        return revWeixin;
    }

    public void setRevWeixin(String revWeixin) {
        this.revWeixin = revWeixin;
    }

    @Override
    public String toString() {
        return "AlarmNoticeUser [policyCode=" + policyCode + ", recevierType=" + recevierType + ", groupCode="
                + groupCode + ", userId=" + userId + ", userAccount=" + userAccount + ", userName=" + userName
                + ", email=" + email + ", mobile=" + mobile + ", weixin=" + weixin + ", revMail=" + revMail
                + ", revSms=" + revSms + ", revWeixin=" + revWeixin + "]";
    }

}

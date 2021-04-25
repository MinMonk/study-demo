package com.monk.app.propertites;

/**
 * @ClassName SocialProperties
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/3
 * @Version V1.0
 **/
public class SocialProperties {

    private String processesUrl = "/auth";

    private QQProperties qq = new QQProperties();

    private WeChatProperties wechat = new WeChatProperties();

    public String getProcessesUrl() {
        return processesUrl;
    }

    public void setProcessesUrl(String processesUrl) {
        this.processesUrl = processesUrl;
    }

    public QQProperties getQq() {
        return qq;
    }

    public void setQq(QQProperties qq) {
        this.qq = qq;
    }

    public WeChatProperties getWechat() {
        return wechat;
    }

    public void setWechat(WeChatProperties wechat) {
        this.wechat = wechat;
    }
}

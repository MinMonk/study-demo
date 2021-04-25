package com.monk.app.social.wechat.api;

public interface WeChat {
    /**
     * 获取微信用户信息
     * @param openId
     * @return
     */
    WeChatUserInfo getUserInfo(String openId);
}

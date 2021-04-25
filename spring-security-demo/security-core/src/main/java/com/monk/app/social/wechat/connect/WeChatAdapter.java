package com.monk.app.social.wechat.connect;

import com.monk.app.social.wechat.api.WeChat;
import com.monk.app.social.wechat.api.WeChatUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * @ClassName WeChatAdapter
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/4
 * @Version V1.0
 **/
public class WeChatAdapter implements ApiAdapter<WeChat> {

    private String openId;

    public WeChatAdapter() {
    }


    public WeChatAdapter(String openId) {
        this.openId = openId;
    }

    @Override
    public boolean test(WeChat api) {
        return true;
    }

    @Override
    public void setConnectionValues(WeChat api, ConnectionValues values) {
        WeChatUserInfo userInfo = api.getUserInfo(openId);
        values.setDisplayName(userInfo.getNickname());
        values.setImageUrl(userInfo.getHeadimgurl());
        values.setProviderUserId(userInfo.getOpenid());
    }

    @Override
    public UserProfile fetchUserProfile(WeChat api) {
        return null;
    }

    @Override
    public void updateStatus(WeChat api, String message) {
        /// do nothing
    }
}

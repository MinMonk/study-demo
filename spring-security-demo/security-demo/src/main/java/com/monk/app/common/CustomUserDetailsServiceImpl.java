package com.monk.app.common;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

@Component("userDetailsService")
public class CustomUserDetailsServiceImpl implements UserDetailsService, SocialUserDetailsService {

    public static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        logger.info("社交登录的用户名：", userId);
        return buildUser(userId);
    }

    private SocialUserDetails buildUser(String user) {
        String pwd = passwordEncoder.encode("123456");
        return new SocialUser(user, pwd,
                true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_admin, ROLE_USER"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("表单登录的用户名：{}", username);
        username = StringUtils.isBlank(username) ? "tom" : username;
        return buildUser(username);
    }
}

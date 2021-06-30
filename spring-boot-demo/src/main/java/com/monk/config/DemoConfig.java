/**
 * 
 * 文件名：DemoConfig.java
 * 版权： Copyright 2017-2022 CMCC All Rights Reserved.
 * 描述： ESB管理系统
 */
package com.monk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.monk.common.holder.SpringContextHolder;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年6月30日 上午10:37:10
 */
@Configuration
public class DemoConfig {
    
    
    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

}

package com.ruleconfig;

import com.monk.rule.CustomRule;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PowerConfig {

    @Bean
    public IRule iRule(){
        return new CustomRule();
    }
}

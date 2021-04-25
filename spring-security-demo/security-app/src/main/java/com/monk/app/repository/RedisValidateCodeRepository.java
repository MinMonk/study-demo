package com.monk.app.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monk.app.exception.CustomValidateCodeException;
import com.monk.app.propertites.SecurityProperties;
import com.monk.app.validate.bean.ValidateCode;
import com.monk.app.validate.bean.ValidateCodeType;
import com.monk.app.validate.processor.ValidateCodeRepository;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisValidateCodeRepository
 * @Description: TODO
 * @Author Monk
 * @Date 2020/7/5
 * @Version V1.0
 **/
@Repository
public class RedisValidateCodeRepository implements ValidateCodeRepository {

    private static final Logger logger = LoggerFactory.getLogger(RedisValidateCodeRepository.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void saveCode(ServletWebRequest servletWebRequest, ValidateCode validateCode, ValidateCodeType validateCodeType) {
        redisTemplate.opsForValue().set(buildRedisKey(servletWebRequest, validateCodeType), validateCode, securityProperties.getValidateCode().getSmsCode().getExpireIn(), TimeUnit.SECONDS);
    }

    private String buildRedisKey(ServletWebRequest servletWebRequest, ValidateCodeType validateCodeType){
        String deviceId = servletWebRequest.getHeader("deviceId");
        if(StringUtils.isBlank(deviceId)){
            logger.error("The deviceId is null, please set the deviceId in request header.");
            throw new CustomValidateCodeException("请求头中没有找到设备ID，请设置deviceId.");
        }
        return "code:" + validateCodeType.toString().toLowerCase() + ":" + deviceId;
    }

    @Override
    public ValidateCode getCode(ServletWebRequest servletWebRequest, ValidateCodeType validateCodeType) {
        String redisKey = buildRedisKey(servletWebRequest, validateCodeType);
        Object value = redisTemplate.opsForValue().get(redisKey);
        if(null == value){
            logger.warn("Not found the value from redis by key : [{}]. ", redisKey);
            return  null;
        }

        return (ValidateCode)value;
    }

    @Override
    public void removeCode(ServletWebRequest servletWebRequest, ValidateCodeType validateCodeType) {
        String redisKey = buildRedisKey(servletWebRequest, validateCodeType);
        redisTemplate.delete(redisKey);
    }
}

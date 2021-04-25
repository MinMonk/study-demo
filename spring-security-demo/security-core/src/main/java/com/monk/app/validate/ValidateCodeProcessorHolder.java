package com.monk.app.validate;

import com.monk.app.exception.CustomValidateCodeException;
import com.monk.app.validate.bean.ValidateCodeType;
import com.monk.app.validate.processor.ValidateCodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName ValidateCodeProcessorHolder
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/3
 * @Version V1.0
 **/
@Component
public class ValidateCodeProcessorHolder {

    @Autowired
    private Map<String, ValidateCodeProcessor> validateCodeProcessorMap;

    public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType validateCodeType) {
        return findValidateCodeProcessor(validateCodeType.toString().toLowerCase());
    }

    public ValidateCodeProcessor findValidateCodeProcessor(String type){
        String name = type.toLowerCase() + ValidateCodeProcessor.class.getSimpleName();
        ValidateCodeProcessor validateCodeProcessor = validateCodeProcessorMap.get(name);
        if(null == validateCodeProcessor){
            throw new CustomValidateCodeException("验证码处理器【" + name + "】不存在！");
        }

        return validateCodeProcessor;
    }
}

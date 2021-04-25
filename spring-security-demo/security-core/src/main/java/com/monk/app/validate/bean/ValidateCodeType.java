package com.monk.app.validate.bean;

import com.monk.app.constant.SecurityConstant;

/**
 * @ClassName ValidateCodeType
 * @Description: TODO
 * @Author Monk
 * @Date 2020/4/12
 * @Version V1.0
 **/
public enum ValidateCodeType {
    SMS{
        @Override
        public String getParamNameOnValidate() {
            return SecurityConstant.DEFAULT_PARAMETER_NAME_SMS_CODE;
        }
    },
    IMAGE{
        @Override
        public String getParamNameOnValidate() {
            return SecurityConstant.DEFAULT_PARAMETER_NAME_IMAGE_CODE;
        }
    };

    public abstract String getParamNameOnValidate();

}

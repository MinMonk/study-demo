package com.monk.app.validate.processor;

import com.monk.app.validate.bean.ValidateCode;
import com.monk.app.validate.bean.ValidateCodeType;
import org.springframework.web.context.request.ServletWebRequest;

public interface ValidateCodeRepository {

    void saveCode(ServletWebRequest servletWebRequest, ValidateCode validateCode, ValidateCodeType validateCodeType);

    ValidateCode getCode(ServletWebRequest servletWebRequest, ValidateCodeType validateCodeType);

    void removeCode(ServletWebRequest servletWebRequest, ValidateCodeType validateCode);
}

package com.monk.app.code;

import com.monk.app.validate.processor.image.ImageCode;
import com.monk.app.validate.generator.CodeGenerator;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @ClassName CustomImageCodeGenerator
 * @Description: TODO
 * @Author Monk
 * @Date 2020/4/6
 * @Version V1.0
 **/
//@Component("imageCodeGenerator")
public class CustomImageCodeGenerator implements CodeGenerator {

    @Override
    public ImageCode generateCode(ServletWebRequest request) {
        System.out.println("自定义验证码生成器");
        return null;
    }
}

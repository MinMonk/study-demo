package com.monk.app.controller;

import com.monk.app.validate.processor.ValidateCodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @ClassName ValidateCodeController
 * @Description: TODO
 * @Author Monk
 * @Date 2020/4/6
 * @Version V1.0
 **/
@RestController
public class ValidateCodeController {

    @Autowired
    private Map<String, ValidateCodeProcessor> validateCodeProcessors;

    @GetMapping("code/{type}")
    public void createImageCode(@PathVariable String type, HttpServletRequest request, HttpServletResponse response) throws Exception {
        validateCodeProcessors.get(type + "ValidateCodeProcessor").createCode(new ServletWebRequest(request, response));
    }
}

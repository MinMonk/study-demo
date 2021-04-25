package com.monk.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

/**
 * @ClassName AsyncController
 * @Description: TODO
 * @Author Monk
 * @Date 2020/4/5
 * @Version V1.0
 **/
@RestController
@RequestMapping("/order")
public class AsyncController {
    public static final Logger logger = LoggerFactory.getLogger(AsyncController.class);


    /**
     * 功能描述: <br>
     * 〈同步方式下单〉
     * @Param: []
     * @Return: java.lang.String
     * @Author: Monk
     * @Date: 2020/4/5 15:22
     */
    @GetMapping
    public String order() throws InterruptedException {
        logger.info("主线程开始");
        Thread.sleep(1000);
        logger.info("主线程结束");
        return "success";
    }

    /**
     * 功能描述: <br>
     * 〈异步方式下单〉
     * @Param:
     * @Return: java.util.concurrent.Callable<java.lang.String>
     * @Author: Monk
     * @Date: 2020/4/5 15:22
     */
    @GetMapping("/async")
    public Callable<String> orderAsync(){
        logger.info("主线程开始");
        Callable<String> result = new Callable<String>() {
            @Override
            public String call() throws Exception {
                logger.info("副线程开始");
                Thread.sleep(1000);
                logger.info("副线程结束");
                return "success";
            }
        };
        logger.info("主线程结束");
        return result;
    }
}

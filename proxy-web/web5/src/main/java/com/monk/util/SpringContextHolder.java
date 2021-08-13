/**
 * 文件名：SpringContextHolder.java
 * 版权： Copyright 2017-2022 CMCC All Rights Reserved.
 * 描述： ESB管理系统
 */
package com.monk.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 以静态变量保存Spring ApplicationContext.
 * 
 * @author huangyulan
 * @version V1.0
 * @date 2018年3月7日 下午4:21:30
 */
public class SpringContextHolder implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * 
     * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
     * 
     * @param applicationContext
     *            应用上下文
     * @author xiaoling
     * @date 2018年4月5日 上午10:49:32
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextHolder.applicationContext = applicationContext;
    }

    /**
     * 
     * 取得存储在静态变量中的ApplicationContext
     * 
     * @return ApplicationContext对象
     * @author xiaoling
     * @date 2018年4月5日 上午10:50:46
     */
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }

    /**
     * 
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型
     * 
     * @param name
     *            类注入的名称
     * @return 实体类
     * @author xiaoling
     * @date 2018年4月5日 上午10:51:04
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型
     * 
     * @param clazz
     *            类
     * @return 实体类
     * @author xiaoling
     * @date 2018年4月5日 上午10:51:54
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return (T) applicationContext.getBeansOfType(clazz);
    }

    /**
     * 
     * 清除applicationContext静态变量
     * 
     * @author xiaoling
     * @date 2018年4月5日 上午10:53:09
     */
    public static void cleanApplicationContext() {
        applicationContext = null;
    }

    /**
     * 
     * 检查 applicationContext静态变量是否注入
     * 
     * @author xiaoling
     * @date 2018年4月5日 上午10:53:26
     */
    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
        }
    }
}

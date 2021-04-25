package com.monk;

import com.monk.config.AppConfig;
import com.monk.framework.CustomApplicationContext;
import com.monk.service.UserService;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws ClassNotFoundException {
        CustomApplicationContext applicationContext = new CustomApplicationContext(AppConfig.class);
        UserService service = (UserService)applicationContext.getBean("userservice");
        service.sayHello();

    }
}

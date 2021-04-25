package com.monk.service;

import com.monk.framework.BeanNameAware;
import com.monk.framework.ScopeType;
import com.monk.framework.annotation.Autowired;
import com.monk.framework.annotation.Componment;
import com.monk.framework.annotation.Scope;
import org.apache.commons.lang.StringUtils;

/**
 * @ClassName UserService
 * @Description: TODO
 * @Author Monk
 * @Date 2020/10/27
 * @Version V1.0
 **/
@Componment
@Scope("prototype")
public class UserService implements BeanNameAware {

    @Autowired()
    private OrderService orderService;

    private String beanName;

    public void sayHello(){
        orderService.order();
        System.out.println("hello word");
        System.out.println("beanName:" + beanName);
    }

    @Override
    public void setName(String name) {
        this.beanName = name;
    }
}

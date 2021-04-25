package com.monk.service;

import com.monk.framework.annotation.Componment;

/**
 * @ClassName OrderService
 * @Description: TODO
 * @Author Monk
 * @Date 2020/10/27
 * @Version V1.0
 **/
@Componment("orderService")
public class OrderService {

    public void order(){
        System.out.println("order");
    }
}

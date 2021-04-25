/**
 * 
 * 文件名：TestWeather.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.webservice.test;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.monk.webservice.axis.WeatherWebServiceLocator;
import com.monk.webservice.axis.WeatherWebServiceSoapStub;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2019年4月28日 下午5:01:48
 */
public class TestAXISWeather {
    public static void main(String[] args) throws ServiceException, RemoteException {
        WeatherWebServiceLocator locator = new WeatherWebServiceLocator();
        WeatherWebServiceSoapStub service = (WeatherWebServiceSoapStub) locator
                .getPort(WeatherWebServiceSoapStub.class);
//        invokeGetSupportProvince(service);
        System.out.println("...................");
//        invokeGetSupportCity(service);
        invokeGetWeatherByOneCity(service);
    }

    // 调用获取支持的省份、州接口
    public static void invokeGetSupportProvince(WeatherWebServiceSoapStub service) throws RemoteException {
        String[] provices = service.getSupportProvince();
        System.out.println("总共" + provices.length + "个");
        int count = 0;
        for (String str : provices) {
            if (0 != count && count % 5 == 0) {
                System.out.println();
            }
            System.out.print(str + "\t");
            count++;
        }
    }

    // 调用获取支持查询某个省份内的城市接口
    public static void invokeGetSupportCity(WeatherWebServiceSoapStub service) throws RemoteException {
        String provinceName = "江苏";
        String[] cities = service.getSupportCity(provinceName);
        System.out.println("总共" + cities.length + "个市");
        for (int i = 0; i < cities.length; i++) {
            if (0 != i && i % 5 == 0) {
                System.out.println();
            }
            System.out.print(cities[i] + "\t");
        }
    }

    // 调用查询某个城市天气的接口
    public static void invokeGetWeatherByOneCity(WeatherWebServiceSoapStub service) throws RemoteException {
        String cityName = "深圳";
        String[] weatherInfo = service.getWeatherbyCityName(cityName);
        for (String str : weatherInfo) {
            System.out.println(str);
        }
    }
}

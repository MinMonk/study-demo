/**
 * 
 * 文件名：dmeo.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.test;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年4月15日 下午2:26:19
 */
public class Demo {
    public static void main(String[] args) {
        System.out.println(expandded_form(12L));
        System.out.println(expandded_form(42L));
        System.out.println(expandded_form(70304L));
        System.out.println(expandded_form(null));
    }
    
    
    private static String expandded_form(Long num) {
        if(null == num || num <= 0) {
            return "''";
        }
        
        StringBuilder stringBuilder = new StringBuilder();
        String numStr = String.valueOf(num);
        int numLength = numStr.length();
        stringBuilder.append("'");
        for(int i = 0; i < numLength; i++) {
            char c = numStr.charAt(i);
            String str = String.format("%-" + (numLength - i) + "s", c).replace(' ', '0');
            stringBuilder.append(str);
            if(i < numLength-1) {
                stringBuilder.append(" + ");
            }
        }
        stringBuilder.append("'");
        return stringBuilder.toString();
    }
}

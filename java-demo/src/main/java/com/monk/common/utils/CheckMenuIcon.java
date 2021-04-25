/**
 * 
 * 文件名：CheckMenuIcon.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2019年4月29日 下午5:35:54
 */
public class CheckMenuIcon {
    public static void main(String[] args) {
        // 读取菜单图标配置文件
        List<String> iconList = readPerperties(
                "D:\\workspace_4.9\\utils\\src\\main\\java\\sys_menu_favoritem_icon.properties");

        // 读取菜单访问记录配置文件
        List<String> accessList = readPerperties(
                "D:\\workspace_4.9\\utils\\src\\main\\java\\sys_access_logging.properties");

        // 查询数据库
        Map<String, String> dbResult = queryDBMenu();
        List<String> notIn = new ArrayList<String>();
        notIn.add("服务设计>>>REST设计");
        notIn.add("系统管理>>>审计信息");

        String temp = "";
        /*System.out.println("----------menu_icon中缺少的配置菜单--------------");
        int num = 249;
        for (String key : dbResult.keySet()) {
            temp = key.replaceAll("=", "/");
            if (!iconList.contains(temp) && !notIn.contains(dbResult.get(key))) {
                System.out.println(dbResult.get(key) + "--" + temp + "=images/index/u" + (num++) + ".png");
                //System.out.println(temp + "=images/index/u" + (num++) + ".png");
            }
        }*/

        System.out.println("----------access_logging中缺少的配置菜单--------------");

        for (String key : dbResult.keySet()) {
            temp = key;
            if(temp.contains("?")) {
                temp = temp.replaceAll("\\?", "");
            }
            if(temp.contains("=")) {
                temp = temp.replaceAll("=", "");
            }
            temp = "/ESBConsole/main.do/ESBConsole/" + temp;
            String menuName = dbResult.get(key);
            if (!accessList.contains(temp) && !notIn.contains(menuName)) {
                String name = menuName.substring(menuName.lastIndexOf(">") + 1, menuName.length());
//                System.out.println(menuName + "--" + temp  + "=" + StringUtils.string2Unicode(name));
                System.out.println(temp  + "=" + StringUtils.string2Unicode(name).replaceAll("\"", ""));
            }
        }
    }

    public static Map<String, String> queryDBMenu() {
        Map<String, String> result = new HashMap<String, String>();
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("SELECT B.MENU_NAME || '>>>' || A.MENU_NAME as MENU_NAME, A.URL FROM  ");
        sqlBuffer.append("(SELECT MENU_NAME, MENU_URL AS URL, SUP_ID FROM SOA_SYS_MENU ");
        sqlBuffer.append("WHERE SUP_ID <> -1 AND ENABLED_FLAG = 'Y') A ");
        sqlBuffer.append("LEFT JOIN (SELECT MENU_ID, MENU_NAME FROM SOA_SYS_MENU  ");
        sqlBuffer.append("WHERE SUP_ID = -1 AND ENABLED_FLAG = 'Y') B ");
        sqlBuffer.append("ON A.SUP_ID = B.MENU_ID WHERE B.MENU_ID IS NOT NULL ");
        Connection conn = null;
        Statement stm = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            stm = conn.createStatement();
            rs = stm.executeQuery(sqlBuffer.toString());
            while (rs.next()) {
                String menuName = rs.getString("MENU_NAME").trim();
                String menuUrl = rs.getString("URL").trim();
                result.put(menuUrl, menuName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<String> readPerperties(String path) {
        List<String> result = new ArrayList<String>();
        try {
            FileReader reader = new FileReader(new File(path));
            BufferedReader br = new BufferedReader(reader);
            String line;
            String url = "";
            while ((line = br.readLine()) != null) {
                if (line.length() > 0) {
                    if (line.contains("=")) {
                        url = line.split("=")[0].trim();
                        // System.out.println(url);
                        result.add(url);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}

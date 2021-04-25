/**
 * 
 * 文件名：TestZip.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.demo;

import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2020年4月18日 下午7:15:59
 */
public class TestZipDemo {
    public static void main(String[] args) {
        
        try {
            String path = getProxyServiceFilePath("C:\\Users\\Administrator\\Desktop\\OSB_SAP_RFC_HQ_ZFM_YLXH_PAS_OL.v0.jar");
            path = path.substring(0, path.lastIndexOf("."));
            String projectName = path.substring(0, path.indexOf("/"));
            String serviceName = path.substring(path.lastIndexOf("/") + 1);
            System.out.println(path);
            System.out.println(projectName);
            System.out.println(serviceName);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    private static String getProxyServiceFilePath(String path) throws Exception {
        String result = "";
        ZipFile zipFile = new ZipFile(path);
        Enumeration<? extends ZipEntry> zipEntrys = zipFile.entries();
        while (zipEntrys.hasMoreElements()) {
            ZipEntry zipEntry = zipEntrys.nextElement();
            String fileName = zipEntry.getName();
            if (fileName.endsWith(".ProxyService")) {
                result = fileName;
                break;
            }

        }
        return result;
    }
}

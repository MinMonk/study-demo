/**
 * 
 * 文件名：TestVelocityEngine.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.demo;

import java.io.File;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2020年4月17日 下午10:27:06
 */
public class VelocityEngineDemo {   
    
    public static void main(String[] args) {
        try {
            VelocityEngine velocityEngine = new VelocityEngine(getVelocityProperties("D:\\apps\\lightcsb\\osb12proxyservice\\SAP\\proxyservice\\business"));
            Map<String, Object> velocityContext = new HashMap<String, Object>();
            velocityContext.put("projectPathOSB", "monk");
            velocityContext.put("servicenameen", "test-service");
            VelocityContext context = initVelocityContext(velocityContext);
            
            StringWriter writer = new StringWriter();
            velocityEngine.mergeTemplate("service.BusinessService.vm", "utf-8", context, writer);
            writeTemplateFile(writer, "D:\\test_file.txt");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private static Properties getVelocityProperties(String templateFileDir) {
        Properties properties = new Properties();
        properties.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, templateFileDir);
        return properties;
    }
    
    private static void writeTemplateFile(StringWriter writer, String targetFilePath) throws Exception {
        File objectFile = new File(targetFilePath);
        try {
            if (!objectFile.getParentFile().exists()) {
                objectFile.getParentFile().mkdirs();
            }
            if (!objectFile.exists()) {
                objectFile.createNewFile();
            }
            PrintStream print = new PrintStream(objectFile, "UTF-8");
            print.print(writer.toString());
            print.close();
        } catch (Exception e) {
            throw e;
        }
    }
    
    private static VelocityContext initVelocityContext(Map<String, Object> velocityContext) {
        // 实例化一个VelocityContext
        VelocityContext context = new VelocityContext();
        // 向VelocityContext中放入键值
        for (String key : velocityContext.keySet()) {
            context.put(key, velocityContext.get(key));
        }
        return context;
    }

}

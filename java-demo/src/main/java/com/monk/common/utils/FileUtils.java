/**
 * 
 * 文件名：FileUtils.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.common.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2018年11月27日 下午3:58:29
 */
public class FileUtils {
    
    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);
    /**
     * 创建文件
     * @param path   文件生成路径
     * @param content  文件内容
     * @author Monk
     * @date 2018年11月27日 下午4:00:17
     */
    public static void createFile(String path, String content) {
        logger.info("create file start. " + path);
        String dir = path.substring(0, path.lastIndexOf("\\"));
        File dirPath = new File(dir);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        File file = new File(path);
        try {
            FileWriter fw = new FileWriter(file, false);
            fw.write(content);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("create file success. " + path);
    }

}

/**
 * 
 * 文件名：SQLUtils.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2018年12月10日 上午11:10:20
 */
public class SQLUtils {

    public static void main(String[] args) throws Exception {
        initSQL("D:/test.txt");
    }

    public static void initSQL(String file_path) throws Exception {
        if (StringUtils.isBlank(file_path)) {
            throw new NullPointerException();
        }

        File file = new File(file_path);
        if (!file.exists()) {
            throw new FileNotFoundException(file_path + " is not exists.");
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            System.out.println("StringBuilder sqlBuilder = new StringBuilder();");
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                // System.out.println("line " + line + ": " + tempString);
                // line++;
                if (StringUtils.isBlank(tempString)) {
                    continue;
                }
                System.out.println("sqlBuilder.append(\"" + tempString.trim() + " \");");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
}

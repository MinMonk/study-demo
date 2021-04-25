/**
 * 
 * 文件名：ReadXml.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.demo;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2019年11月8日 下午5:25:45
 */
public class ReadXmlDemo {
    public static void main(String[] args) {
        SAXReader reader = new SAXReader();
        Document document;
        try {
            document = reader.read("D:\\gr.xml");
            document.setXMLEncoding("GBK");
            Element root = document.getRootElement();
            List<String> elements = root.content();
            for(String str : elements) {
                System.out.println(str);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}

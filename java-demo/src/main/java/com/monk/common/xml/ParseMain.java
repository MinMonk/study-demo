/**
 * 
 * 文件名：ParseMain.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.common.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年3月25日 上午10:55:47
 */
public class ParseMain {
    
    public static void main(String[] args) {
        Map<String, String> keyWord = initKeyWord();
        // 该实例中的xml文件就在当前package下
        String path = "F:\\StudyCenter\\Code\\StudyDemo\\java-demo\\src\\main\\java\\com\\monk\\common\\xml\\ws.xml";
        String content = readFile(path);
        XpathAnalyseXml demo = new XpathAnalyseXml();
        List<EsbPointField> fieldList = initFieldData();
        List<EsbPointFieldLink> linkList = initLinkData();
        List<Map<String, Object>> result = demo.analyseByKeyWord(fieldList, linkList, keyWord, content);
        printMap(result);
    }
    
    /**
     * 格式化对象为Json字符串并输出
     * @param obj  对象
     * @author Monk
     * @date 2021年4月12日 下午5:43:11
     */
    private static void printMap(Object obj) {
        try {
            ObjectMapper objMapper = new ObjectMapper();
            System.out.println(objMapper.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 初始化字段映射关系数据
     * @return  字段映射关系数据
     * @author Monk
     * @date 2021年4月12日 下午5:43:29
     */
    private static List<EsbPointFieldLink> initLinkData() {
        List<EsbPointFieldLink> result = new ArrayList<EsbPointFieldLink>();
        result.add(new EsbPointFieldLink("docNum", "DOCUMENT_NUM", "//INPUTCOLLECTION/INPUTCOLLECTION_ITEM"));
        result.add(new EsbPointFieldLink("docCode", "DOCUMENT_CODE", "//INPUTCOLLECTION/INPUTCOLLECTION_ITEM"));
        result.add(new EsbPointFieldLink("docDesc", "DOCUMENT_DESCRIPTION", "//INPUTCOLLECTION/INPUTCOLLECTION_ITEM"));
        result.add(new EsbPointFieldLink("docBy", "DOCUMENT_CREATE_BY", "//INPUTCOLLECTION/INPUTCOLLECTION_ITEM"));
        result.add(new EsbPointFieldLink("transferTime", "TRANSFER_TIME", "//INPUTCOLLECTION/INPUTCOLLECTION_ITEM"));
        result.add(new EsbPointFieldLink("budgetStatus", "SCOPE_FLAG", "//INPUTCOLLECTION/INPUTCOLLECTION_ITEM"));
        result.add(new EsbPointFieldLink("sourceSystem", "SOURCE_SYSTEM", "//INPUTCOLLECTION/INPUTCOLLECTION_ITEM"));

        result.add(new EsbPointFieldLink("lineNum", "DOCUMENT_LINE_NUM", "//TRANSACTION_LINES/TRANSACTION_LINES_ITEM"));
        result.add(new EsbPointFieldLink("projNum", "PROJECT_NUM", "//TRANSACTION_LINES/TRANSACTION_LINES_ITEM"));
//        result.add(new EsbPointFieldLink("deptCode", "BUDGET_DEPT_CODE", "//TRANSACTION_LINES/TRANSACTION_LINES_ITEM"));
        result.add(new EsbPointFieldLink("transcationDate", "TRANSACTION_DATE",
                "//TRANSACTION_LINES/TRANSACTION_LINES_ITEM"));

//        result.add(new EsbPointFieldLink("name", "NAME", "//TEST_ELEMENT/TEST_ELEMENT_ITEM"));
//        result.add(new EsbPointFieldLink("age", "AGE", "//TEST_ELEMENT/TEST_ELEMENT_ITEM"));
//        
//        result.add(new EsbPointFieldLink("province", "PROVINCE", "//ADDRESS/ADDRESS_ITEM"));
//        result.add(new EsbPointFieldLink("city", "CITY", "//ADDRESS/ADDRESS_ITEM"));
        return result;
    }

    
    /**
     * 初始化字段配置信息
     * @return  字段配置信息
     * @author Monk
     * @date 2021年4月12日 下午5:43:52
     */
    private static List<EsbPointField> initFieldData() {
        List<EsbPointField> fieldList = new ArrayList<EsbPointField>();
        fieldList.add(new EsbPointField("docNum", 1, "TEXT"));
        fieldList.add(new EsbPointField("docCode", 1, "TEXT"));
        fieldList.add(new EsbPointField("docDesc", 1, "TEXT"));
        fieldList.add(new EsbPointField("docBy", 1, "TEXT"));
        fieldList.add(new EsbPointField("transferTime", 1, "DATE"));
        fieldList.add(new EsbPointField("lineNum", 2, "TEXT"));
        fieldList.add(new EsbPointField("projNum", 2, "TEXT"));
        fieldList.add(new EsbPointField("deptCode", 2, "TEXT"));
        fieldList.add(new EsbPointField("transcationDate", 2, "DATE"));

//        fieldList.add(new EsbPointField("name", 3, "TEXT"));
//        fieldList.add(new EsbPointField("age", 3, "TEXT"));
//        
//        fieldList.add(new EsbPointField("province", 4, "TEXT"));
//        fieldList.add(new EsbPointField("city", 4, "TEXT"));
        return fieldList;
    }
    
    private static Map<String, String> initKeyWord() {
        Map<String, String> keyWord = new HashMap<String, String>();
        keyWord.put("DOCUMENT_NUM", "1432");
        keyWord.put("PROJECT_NUM", "481010ZD0000200002");
//        keyWord.put("DOCUMENT_LINE_NUM", "36542477");
//        keyWord.put("NAME", "张三");
//        keyWord.put("PROVINCE", "湖北");
//       keyWord.put("CITY", "宜昌");
//        keyWord.put("DOCUMENT_CODE", "302210C27210305004");
//        keyWord.put("BUDGET_DEPT_CODE", "4591094_1_2");
//        keyWord.put("TRANSACTION_DATE", "4591094_1_2");
        return keyWord;
    }
    
    /**
     * 将文件读成字符串
     * @param path  xml文件路径
     * @return  字符串
     * @author Monk
     * @date 2021年4月12日 下午5:44:11
     */
    private static String readFile(String path){
        String content = null;
        try {
            content = FileUtils.readFileToString(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}

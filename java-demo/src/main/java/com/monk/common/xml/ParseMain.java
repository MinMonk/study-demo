/**
 * 
 * 文件名：ParseMain.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.common.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monk.common.utils.GZIPUtil;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年3月25日 上午10:55:47
 */
public class ParseMain {
    
    public static String formatXML(String inputXML) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new StringReader(inputXML));
        String requestXML = null;
        XMLWriter writer = null;
        if (document != null) {
          try {
            StringWriter stringWriter = new StringWriter();
            OutputFormat format = new OutputFormat(" ", true);
            writer = new XMLWriter(stringWriter, format);
            writer.write(document);
            writer.flush();
            requestXML = stringWriter.getBuffer().toString();
          } finally {
            if (writer != null) {
              try {
                writer.close();
              } catch (IOException e) {
              }
            }
          }
        }
        return requestXML;
      }
 
    
    public static void main(String[] args) throws Exception{
        Map<String, String> keyWord = initKeyWord();
        // 该实例中的xml文件就在当前package下
        String path = "F:\\StudyCenter\\Code\\study-demo\\java-demo\\src\\main\\java\\com\\monk\\common\\xml\\ws2.xml";
        //String content = readFile(path);
        
        String gzipStr = "{GZIP}H4sIAAAAAAAAALVXzW6jVhTe9ykibyubC9gYW4QRhpsEjfmZeyGdzObKgy8ZSzFYxkmmbzCrqtJ0UalTVeqim7aqqm6qiTpPM4nVt+gFbI/BuGoqbHkB53zfd4/PH0Z58np6dXRD58kkjo4bfAs0jmgUxONJdHnc8L2Tptx4on6mJPFoRqObPoxu6FU8o0eMFiX9lfm48WqxmPU5Lgle0ekoaTFv6mrF80suveDoisc11I3WGR2N6ZzLDf1BPP5yS/VxklEi9M1odr1wR/PRlC7Y78m1PsnEo1YwDYJWEE85K7nMD2+sTmT8aqSDB2TgEuxo5OwZMaezeL4YjC8XiF7RUUJRfL2geH6zCsHCp2dQMyBSFez4SIf4AnvQMg1VtzBqYt1SuJKjALQ1C6ofP3x3//6bv99+WP5xt7z7ocjIEIrnPIW2SsUOCKUQ9MQefRmwb1cOA2nca0tUEsIAKFyOU3wMEWFnCbIMJIVb3+b2VJFbXbsaxl8YR6+TST+aXB03FvNruk4RM25SdHt727oVs1IIAPDcc2uIszI1J1GyGEUBbTBJ7A8s0yOG5kFVAALfBO0m6Ho832/3+oBvye3O50DuAxboNlRxtVNIsPkC1hWI7iMEbY+kwnVpeo6nDQmCuoNqS5iLnHPT1iHRHQNmLaNwRZuCHN8r+LcMioc0BjRriwdB1hTn0CB87YpCXYpcae6yTWC7vqc7wyHUPdOxK43EZNOUe1xkkqfwQhWB1OXDkPa6nTbP05dyp9sJR7wkUrEtB2GYn7VGZ1TD0X0rbS3vwl1VgQ05WxfIyNEVgCLR9i2Vb4tCCZ6ai8CM+8J1BB60WTpK+AplA2IdmW6WgeXdm+XPvy1/v2s+vPn64d0vzfv3b+9//bYkss0oHY4gG00yuFAfvv/q4cefPv75VzmADSJjsla08QlbKZ5pVU8/AGA9/buETAPrDsvZyVA7VVegLUuOyDYjyVdjNhAk27G7vk8tQOBzr67m2wSu5T01NG2I95irG07u0CD4Pw2XaubNUyrExrETxt5FXFWKbc7hkqfpOvLZHt1KXMG0L2mh2H1E0h7ZJhUza7nOf5znR4xpVQ1L5TuHtuGg9H7AlptlWIQHkiBJoijnlCIg4+iOnT4I8sNWeUaQjRZkj5HaNnlBtbZtXlAVD6LaPohq5yCqUr2qQ6hhSDTL8W1PZY0EQDr6//LJe6xEPMw24PbN/q5D3V1TO4SKrczt/xdQ5Vpbi28VzLp5W1ldb78Wqf8AULiitVANAAA=";
        //gzipStr = "{GZIP}H4sIAAAAAAAAAO1aTW/jRBi+8yuqXFHiGefLiVyvHHu29W78wcy4NL1Yqet0K22SKknb5R/sCSEtByRASBy4AEKIC9qK/TXbVvwLxuOP2olBYmXDxVFUue/HM5OZeWbeZ2T5yav5y73rYLW+WC72G7AFGnvBwl+eXSzO9xsufdqUGk+Uj+T1cnoZLK6HaHEdvFxeBnssbbEexub9xovN5nIoCGv/RTCfrlvMG7pay9W5ED4IQZwnNJQU6zCYngUrITIMR8uzzzKo/w5ysRaHxuLyauNMV9N5sGG/J8J6hFlOW/7c91v+ci6Y6/Oo8UbcIssvjrTJyBs5HrFV7/ATz5hfLleb0dn5BgfrYHUd4OXVJiCr67gLJjk4RKqOsCIT28UaIhNCkWnoimYS3CSaKQtbjlygpZpIef/u67u3X/755t3Db7cPt9/lM3iETO3nyFKCdhfMejMwaA+CU599+9LM750NOr2gJ858IAtRnOwShD3WlihJoCcLyb+RPUQU4mdHJeRTfe/V+mK4uHi539isroJkiJgxHaKbm5vWTZtPhQgAFI7NMeHT1LxYrDfThR80GCRxR6ZBPV2lSBGBCJug0wR9CuGwKw7FXmsw6H0MpCFgHc2Gyo56gDxinKCyOqK5GCOLeiFwWZjUpurYw0izcWkD5mD7yLA05Gm2jviSkYW8Tca2S3P+jEGmWGWBRmn9wYgtiiOke7B0RLEsRGGLd3wnsByXavZ4jDRq2Fah0TMYmyKPgw3vOZoo01NRCmC/P+h3OxAGp1K3351NYa8dtDuSP5tFbSXRPFW3NdcMlxadOPEsMJKz7QLrUXRBQD7Rck0FdtriVnhozgfy3BPHFiHosOHYii9A1hHRsOHwEXi4ff3w4y8Pv942719/cf/NT827t2/ufv5qCySbsdU4Roya3mii3H/7+f33P7z//Y/tDqQRPJMtRYs8ZVsKNcxi9gMAEvbvJnCMkasfIOqxnckjVKUuUXAUu+vg8dE+6UUbJaeHx3fcXR8Px4g1hxi1SlvdOdTSVngOtV0JaqcS1G4lqL1KUPuVoEqVoA4qQYWgGthq2AWroReshl+wGoLBahgGq6EYrIZjsBqSwWpYJlbDMrFUlvFSyUPHtExQfsCrUe01NixE/sZcXJgN/O6s8yGFWYgZFVlbBUvqyNYaYZWmaLkqg5vi7iBvt54TCpwMNm9OW8uYMRojlZUwqmm7VqlDzTTDM1bl8l/XkSCA4ERn5RYQ+Z9ktB5jsiOgIyeuJgHoDVh8G2Q+FObG5jE4C6FqWviLFGc8OWZtdgf8A6RcahIUreMR8cI1cGTQSYSnWp0u6Cdl7q4/ao+tHT07CaGIPWCKyMnPjK27Gs2atEPVstA4ayJsIVJ0MMnadFaKWoStzFLZlaKWeoSlqKWeYClqqQdYilrq+ZWilnp8Zfen5CYDNAGTM5CCAZMv7FuoZbI5BVtSxHoFhqxqJYmFITyZG6h6nBh7maxtX3QVpTpesiNCwDbMfiyVM/aIHggbtp5cA4WWmJqP7E5Yw69RtImiWZMILLVEeaaNqXHCRRmOLnPKnIkUHjHSVwZu2hY9rKaiqGVmLTNrmVnLzFpm1jKzlpkVyMyd4mtbUP5nYnP7Or8Wm7XYrMVmLTb/X7EpfpDUhGKtNGulWSvNWmnWSrNWmrXSrJVmrTT/UWkWO2N78WtoRa7Emn+tlVnT12Xj5+x7ucpfXNiHs9ErAAA=";
        String content = GZIPUtil.unzipString(gzipStr);
        System.out.println(content);
        content = formatXML(content);
        System.out.println("=============================");
        System.out.println(content);
        
        
        XpathAnalyseXml demo = new XpathAnalyseXml();
        List<EsbPointField> fieldList = initFieldData();
        List<EsbPointFieldLink> linkList = initLinkData();
        List<Map<String, Object>> analyseResult = demo.analyseByKeyWord(fieldList, linkList, keyWord, content);
        
        printMap(analyseResult);
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
        result.add(new EsbPointFieldLink("priKey", "PRI_KEY", "//INPUTCOLLECTION/INPUTCOLLECTION_ITEM"));
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
        fieldList.add(new EsbPointField("priKey", 1, "TEXT"));
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
//        keyWord.put("PROJECT_NUM", "481010ZD0000200002");
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

/**
 * 
 * 文件名：StringUtils.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.common.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import net.sf.json.xml.XMLSerializer;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2018年11月27日 下午3:35:51
 */
public class StringUtils {

    public static void main(String[] args) {
         String str = "实例号,服务英文名,服务中文名,服务版本,提供方系统,创建时间,重发失败";
         initExcelHeader(str);
        /*Map<String, List<String>> result = generateCalendarStr(2020);
        for(String str : result.get("topHalfYear")) {
            System.out.println(str);
        }
        System.out.println("-----------------------------------------");
        for(String str : result.get("bottomHalfYear")) {
            System.out.println(str);
        }*/
        
        //System.out.println(unicode2String("Copyright 2018 \u6DF1\u5733\u5E02\u8FDC\u884C\u79D1\u6280\u80A1\u4EFD\u6709\u9650\u516C\u53F8 All Rights Reserved"));
        //System.out.println(string2Unicode("张三李四王二麻子"));
         //\u91cd\u53d1\u6210\u529f
         System.out.println(unicode2String("\u91cd\u53d1\u6210\u529f"));
//        String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">    <soapenv:Header xmlns:msg=\"http://soa.monk.com/MsgHeader\" xmlns:jms=\"http://soa.monk.com/JMS_BP_SOA_HQ_DistCurrencySrv\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"></soapenv:Header>    <soapenv:Body xmlns:msg=\"http://soa.monk.com/MsgHeader\" xmlns:jms=\"http://soa.monk.com/JMS_BP_SOA_HQ_DistCurrencySrv\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">        <jms:InputParameters>            <jms:MSGHEADER>                <msg:SOURCESYSTEMID>1</msg:SOURCESYSTEMID>                <msg:SOURCESYSTEMNAME>1</msg:SOURCESYSTEMNAME>                <msg:TOKEN>******</msg:TOKEN>                <msg:USER_ID>1</msg:USER_ID>                <msg:USER_NAME>******</msg:USER_NAME>                <msg:USER_PASSWD>******</msg:USER_PASSWD>                <msg:SUBMIT_DATE>2018-09-20T18:22:51</msg:SUBMIT_DATE>                <msg:PAGE_SIZE>1</msg:PAGE_SIZE>                <msg:CURRENT_PAGE>1</msg:CURRENT_PAGE>                <msg:TOTAL_RECORD>1</msg:TOTAL_RECORD>                <msg:PROVINCE_CODE>1</msg:PROVINCE_CODE>                <msg:ROUTE_CODE>1</msg:ROUTE_CODE>                <msg:TRACE_ID>1</msg:TRACE_ID>                <msg:RESERVED_1>1</msg:RESERVED_1>                <msg:RESERVED_2>1</msg:RESERVED_2>            </jms:MSGHEADER>            <jms:INPUTCOLLECTION>                <!--                Zero or more repetitions:-->                <jms:INPUTCOLLECTION_ITEM>                    <jms:PRI_KEY>1</jms:PRI_KEY>                    <jms:ACTION>1</jms:ACTION>                    <jms:MD_DESCRIPTION>1</jms:MD_DESCRIPTION>                    <jms:MD_CODE>1</jms:MD_CODE>                    <jms:DISTRIBUTION_AREA>1</jms:DISTRIBUTION_AREA>                    <jms:CURRENCY_SYMBOL>1</jms:CURRENCY_SYMBOL>                    <jms:CURRENCY_PRECISION>1</jms:CURRENCY_PRECISION>                    <jms:ENABLE_FLAG>1</jms:ENABLE_FLAG>                    <jms:EFFECTIVE_DATE>2018-09-20T18:22:51</jms:EFFECTIVE_DATE>                    <jms:EXPIRY_DATE>2018-09-20T18:22:51</jms:EXPIRY_DATE>                    <jms:INPUT_EXT>1</jms:INPUT_EXT>                </jms:INPUTCOLLECTION_ITEM>            </jms:INPUTCOLLECTION>        </jms:InputParameters>    </soapenv:Body></soapenv:Envelope>";
//        xmlParseJson(xmlStr);
        
    }
    
    public static void xmlParseJson(String xmlStr) {
        /* 第二种方法，使用json-lib提供的方法 */
        //创建 XMLSerializer对象
        XMLSerializer xmlSerializer = new XMLSerializer();
        //将xml转为json（注：如果是元素的属性，会在json里的key前加一个@标识）
        String result = xmlSerializer.read(xmlStr).toString();
        //输出json内容
        System.out.println(result);
    }

    /**
     * 将一年当中的每一天转换为字符串
     * 
     * @param year
     *            年份
     * @return 日期字符串集合
     * @author Monk
     * @date 2019年3月21日 下午4:53:48
     */
    public static Map<String, List<String>> generateCalendarStr(int year) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();
        List<String> topHalfYear = new ArrayList<String>();
        List<String> bottomHalfYear = new ArrayList<String>();

        Calendar cal = Calendar.getInstance();
        cal.set(year, 0, 1);
        int dayOfYear = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
        topHalfYear.add(formartDate(cal.getTime()));
        for (int i = 0; i < dayOfYear - 1; i++) {
            cal.add(Calendar.DATE, 1);
            String s = formartDate(cal.getTime());
            if (cal.get(Calendar.MONTH) < 6) {
                topHalfYear.add(s);
            } else {
                bottomHalfYear.add(s);
            }
        }

        result.put("topHalfYear", topHalfYear);
        result.put("bottomHalfYear", bottomHalfYear);
        return result;
    }

    /**
     * 首字母转大写
     * 
     * @param str
     * @return
     * @author Monk
     * @date 2018年11月27日 下午3:35:56
     */
    public static String FirstToUpperCase(String str) {
        if (str.length() > 0) {
            String s1 = str.substring(0, 1);
            String s2 = str.substring(1);
            return s1.toUpperCase() + s2.toLowerCase();
        } else {
            return null;
        }
    }

    /**
     * 格式化字符串，转驼峰结构
     * 
     * @param str
     * @return
     * @author Monk
     * @date 2018年11月27日 下午3:48:09
     */
    public static String formartField(String str) {
        StringBuffer buffer = new StringBuffer();
        str = str.toLowerCase();
        if (str.contains("_")) {
            String[] strs = str.split("_");
            buffer.append(strs[0]);
            for (int i = 1; i < strs.length; i++) {
                buffer.append(FirstToUpperCase(strs[i]));
            }
        } else {
            buffer.append(str);
        }
        return buffer.toString();
    }
    
    public static String formatClassName(String str) {
        StringBuffer buffer = new StringBuffer();
        str = str.toLowerCase();
        if (str.contains("_")) {
            String[] strs = str.split("_");
            for (int i = 0; i < strs.length; i++) {
                buffer.append(FirstToUpperCase(strs[i]));
            }
        } else {
            buffer.append(str);
        }
        return buffer.toString();
    }

    public static String string2Unicode(String content) {
        return JSON.toJSONString(content, SerializerFeature.BrowserCompatible);
    }

    public static void initExcelHeader(String header) {
        if (null == header || !header.contains(",")) {
            return;
        }
        String[] headers = header.split(",");
        System.out.println("List<String> header = new ArrayList<String>(" + headers.length + ");");
        for (String str : headers) {
            System.out.println("//" + str);
            System.out.println("header.add(" + string2Unicode(str) + ");");
        }
    }
    
    public static String unicode2String(String content) {
        String result = null;
        byte[] utf8;
        try {
            utf8 = content.getBytes("UTF-8");
            result = new String(utf8, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String formartDate(Date date) {
        notNull(date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(date);
        return dateStr;
    }

    /**
     * 包装成时间字符串
     * 
     * @param year
     *            年份
     * @param month
     *            月份
     * @param day
     *            日期
     * @param separator
     *            日期分隔符
     * @return 时间字符串
     * @author Monk
     * @date 2019年3月21日 下午4:27:09
     */
    public static String generateDateStr(String year, String month, String day, String separator) {
        notNull(year);
        notNull(month);
        notNull(day);
        if (year.length() < 4 || month.length() < 1 || day.length() < 1) {
            throw new IllegalArgumentException("The date must be error.");
        }
        month = month.length() < 2 ? "0" + month : month;
        day = day.length() < 2 ? "0" + day : day;
        String dateStr = year + separator + month + separator + day;
        return dateStr;
    }

    /**
     * Assert that an object is not {@code null} .
     * 
     * <pre class="code">
     * notNull(clazz, "The class must not be null");
     * </pre>
     * 
     * @param object
     *            the object to check
     * @param message
     *            the exception message to use if the assertion fails
     * @throws IllegalArgumentException
     *             if the object is {@code null}
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert that an object is not {@code null} .
     * 
     * <pre class="code">
     * notNull(clazz);
     * </pre>
     * 
     * @param object
     *            the object to check
     * @throws IllegalArgumentException
     *             if the object is {@code null}
     */
    public static void notNull(Object object) {
        notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }
    
    public static boolean isNotEmpty(String in){
        return in != null && !"".equals(in.trim());
    }
    
    public static boolean isNotBlank(String str) {
        return !StringUtils.isBlank(str);
    }
    
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

}

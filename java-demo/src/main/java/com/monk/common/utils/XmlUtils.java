/**
 * 
 * 文件名：XmlUtils.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.common.utils;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;

import net.sf.json.JSONObject;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2019年12月27日 下午5:08:32
 */
public class XmlUtils {
    public static void main(String[] args) {
        String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "\r\n"
                + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n"
                + "  <soapenv:Header xmlns:msg=\"http://soa.monk.com/MsgHeader\" xmlns:osb=\"http://soa.monk.com/OSB_CREDIT_CREDIT_HQ_InquirySyncOrgSrv\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"></soapenv:Header>\r\n"
                + "  <soapenv:Body xmlns:msg=\"http://soa.monk.com/MsgHeader\" xmlns:osb=\"http://soa.monk.com/OSB_CREDIT_CREDIT_HQ_InquirySyncOrgSrv\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">  \r\n"
                + "    <osb:InputParameters> \r\n" + "      <osb:MSGHEADER> \r\n"
                + "        <msg:SOURCESYSTEMID>1</msg:SOURCESYSTEMID>  \r\n"
                + "        <msg:SOURCESYSTEMNAME>1</msg:SOURCESYSTEMNAME>  \r\n"
                + "        <msg:TOKEN>******</msg:TOKEN>  \r\n" + "        <msg:USER_ID>1</msg:USER_ID>  \r\n"
                + "        <msg:USER_NAME>******</msg:USER_NAME>  \r\n"
                + "        <msg:USER_PASSWD>******</msg:USER_PASSWD>  \r\n"
                + "        <msg:SUBMIT_DATE>2019-02-22T10:35:59</msg:SUBMIT_DATE>  \r\n"
                + "        <msg:PAGE_SIZE>1</msg:PAGE_SIZE>  \r\n"
                + "        <msg:CURRENT_PAGE>1</msg:CURRENT_PAGE>  \r\n"
                + "        <msg:TOTAL_RECORD>1</msg:TOTAL_RECORD>  \r\n"
                + "        <msg:PROVINCE_CODE>1</msg:PROVINCE_CODE>  \r\n"
                + "        <msg:ROUTE_CODE>1</msg:ROUTE_CODE>  \r\n"
                + "        <msg:TRACE_ID>1</msg:TRACE_ID>  \r\n"
                + "        <msg:RESERVED_1>1</msg:RESERVED_1>  \r\n"
                + "        <msg:RESERVED_2>1</msg:RESERVED_2> \r\n" + "      </osb:MSGHEADER>  \r\n"
                + "      <osb:INPUTCOLLECTION> \r\n" + "        <!--Zero or more repetitions:-->  \r\n"
                + "        <osb:INPUTCOLLECTION_ITEM> \r\n"
                + "          <osb:INPUT_JSON>{changeTime:\"2018-09-12 00:00:00\",acckey:\"y47681jkx9021xzoq0028663gfropl186043\"}</osb:INPUT_JSON> \r\n"
                + "        </osb:INPUTCOLLECTION_ITEM> \r\n" + "      </osb:INPUTCOLLECTION> \r\n"
                + "    </osb:InputParameters> \r\n" + "  </soapenv:Body>\r\n" + "</soapenv:Envelope>\r\n" + "";
        formatXml(xmlStr);
    }

    public static void formatXml(String xmlStr) {
        StringBuilder xmlStrBuilder = new StringBuilder();
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xmlStr);
            Element rootElt = doc.getRootElement();
            StringBuilder str = readXmlChild(xmlStrBuilder, rootElt, 0);
            System.out.println(str);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归读取XML节点信息，返回格式化后字符串
     * 
     * @param xmlStr
     *            返回的xml字符串
     * @param element
     *            节点
     * @param level
     *            缩进等级
     * @return 格式化后的xml字符串
     * @author Monk
     * @date 2019年12月27日 下午6:36:32
     */
    public static StringBuilder readXmlChild(StringBuilder xmlStr, Element element, int level) {
        String space = getSpace(level);
        level++;

        String name = element.getQualifiedName();
        String value = element.getTextTrim();
        String namespaces = getNameSpace(element);
        
        xmlStr.append(space + "<" + name + namespaces + ">");
        if (element.elements().size() > 0) {
            xmlStr.append("\n");
            List<Element> childs = element.elements();
            for (Element ele : childs) {
                readXmlChild(xmlStr, ele, level);
            }
            xmlStr.append(space + "</" + name + ">\n");
        } else {
            if (org.apache.commons.lang.StringUtils.isBlank(value)) {
                xmlStr.append("</" + name + ">\n");
            } else {
                if (isjson(value)) {
                     String jsonStr = formartJson(value, level);
                     xmlStr.append(jsonStr + "\n");
                     xmlStr.append(space + "</" + name + ">\n");
                }else {
                    xmlStr.append(value + "</" + name + ">\n");
                }
            }
        }
        return xmlStr;
    }

    /**
     * 判断字符串是否是json字符串
     * 
     * @param str
     *            字符串
     * @return 判断结果
     * @author Monk
     * @date 2019年12月27日 下午6:36:03
     */
    private static boolean isjson(String str) {
        try {
            JSONObject jsonObj = JSONObject.fromObject(str);
            if (jsonObj.isEmpty() || jsonObj.isNullObject()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取节点的命名空间
     * 
     * @param element
     *            节点
     * @return 节点的命名空间
     * @author Monk
     * @date 2019年12月27日 下午6:35:30
     */
    private static String getNameSpace(Element element) {
        StringBuilder builder = new StringBuilder();
        if (element.isRootElement()) {
            Namespace ns = element.getNamespace();
            if (null != ns) {
                builder.append(" " + ns.asXML());
            }
        } else {
            List<Namespace> namespaces = element.additionalNamespaces();
            if (null != namespaces && namespaces.size() > 0) {
                for (int i = 0; i < namespaces.size(); i++) {
                    builder.append(" " + namespaces.get(i).asXML());
                }
            }
        }
        return builder.toString();
    }

    /**
     * 获取缩进
     * 
     * @param level
     *            缩进等级
     * @return
     * @author Monk
     * @date 2019年12月27日 下午6:35:50
     */
    private static String getSpace(int level) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < level; i++) {
            str.append("\t");
        }
        return str.toString();
    }
    
    /**
     * 格式化JSON字符串
     * @param jsonStr JSON字符串
     * @param level 缩进等级
     * @return
     * @author Monk
     * @date 2019年12月27日 下午7:10:02
     */
    public static String formartJson(String jsonStr, int level) {
        // 存放格式化的json字符串
        StringBuffer jsonForMatStr = new StringBuffer();
        jsonForMatStr.append("\n");
        for (int index = 0; index < jsonStr.length(); index++)// 将字符串中的字符逐个按行输出
        {
            // 获取s中的每个字符
            char c = jsonStr.charAt(index);
            // System.out.println(s.charAt(index));

            // level大于0并且jsonForMatStr中的最后一个字符为\n,jsonForMatStr加入\t
            if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                jsonForMatStr.append(getSpace(level));
            }
            // 遇到"{"和"["要增加空格和换行，遇到"}"和"]"要减少空格，以对应，遇到","要换行
            switch (c) {
            case '{':
            case '[':
                jsonForMatStr.append(c + "\n");
                level++;
                break;
            case ',':
                jsonForMatStr.append(c + "\n");
                break;
            case '}':
            case ']':
                jsonForMatStr.append("\n");
                level--;
                jsonForMatStr.append(getSpace(level));
                jsonForMatStr.append(c);
                break;
            default:
                jsonForMatStr.append(c);
                break;
            }
        }
        return jsonForMatStr.toString();
    }
}

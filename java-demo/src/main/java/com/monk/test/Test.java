package com.monk.test;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import net.sf.json.JSONObject;

public class Test {
    public static void main(String[] args) throws Exception {
        String path = "D:\\testDir\\aaaa.txt";
        String xmlReportString = FileUtils.readFileToString(new File(path));
        System.out.println(xmlReportString);
        
        Document doc = DocumentHelper.parseText(xmlReportString);
        Element rootElt = doc.getRootElement();
        Element bodyElt = rootElt.element("Body");
        if (null == bodyElt) {
            System.out.println("body is null.");
        }
        List<Element> bodyChildList = bodyElt.elements();
        Element parametersElt = null;
        if (null != bodyChildList && bodyChildList.size() > 0) {
            parametersElt = bodyChildList.get(0);
        }
        Set<String> paraSet = new HashSet<String>();
        paraSet.add("INPUTCOLLECTION");
        if (null != parametersElt) {
            System.out.println(parametersElt);
            JSONObject obj = iterateElement(parametersElt, paraSet);
            System.out.println(obj.toString(2, 1));
        }
    }
    
    public static JSONObject iterateElement(Element element, Set<String> paraSet) {
        List<Element> node = element.elements();
        Element et = null;
        JSONObject obj = new JSONObject();
        List list = null;
        for (int i = 0; i < node.size(); i++) {
            list = new LinkedList();
            et = (Element) node.get(i);
            if (!StringUtils.isNotEmpty(et.getTextTrim())) {
                if (et.elements().size() == 0) {
                    obj.put(et.getName(), et.getTextTrim());
                    continue;
                }
                if (obj.containsKey(et.getName())) {
                    list = (List) obj.get(et.getName());
                }
                if ((null != paraSet && paraSet.contains(et.getName()))) {
                    list.add(iterateElement(et, paraSet));
                    obj.put(et.getName(), list);
                } else {
                    obj.put(et.getName(), iterateElement(et, paraSet));
                }
            } else {
                if (obj.containsKey(et.getName())) {
                    list = (List) obj.get(et.getName());
                    list.add(et.getTextTrim());
                    obj.put(et.getName(), list);
                } else {
                    obj.put(et.getName(), et.getTextTrim());
                }
            }
        }
        
        return obj;
    }
}

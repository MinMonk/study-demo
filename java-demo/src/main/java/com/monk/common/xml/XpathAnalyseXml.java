/**
 * 
 * 文件名：AnalyseXmlServiceImpl.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.common.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XPath解析Xml类
 * 
 * @author Monk
 * @version V1.0
 * @date 2021年3月27日 下午5:19:49
 */
public class XpathAnalyseXml {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(XpathAnalyseXml.class);

    /**
     * 解析方向：向上解析
     */
    private static final String ANALYSE_DIRECTION_TOP = "TOP";

    /**
     * 解析方向：向下解析
     */
    private static final String ANALYSE_DIRECTION_DOWN = "DOWN";

    /**
     * 节点名称后缀
     */
    private static final String NODE_NAME_SUFFIX = "_DATA";
    
    private static final String KEY_XPATH = "xPath";
    private static final String KEY_LEVEL = "level";

    /**
     * 根据XPath结合关键字信息解析XML信息
     * 
     * @param fieldList
     *            端对端配置编码
     * @param linkList
     *            服务配置编码
     * @param keyWord
     *            关键字信息
     * @param content
     *            xml文本内容
     * @return 解析之后的XML内容
     * @author Monk
     * @date 2021年3月27日 下午6:29:53
     */
    public List<Map<String, Object>> analyseByKeyWord(List<EsbPointField> fieldList,
            List<EsbPointFieldLink> linkList, Map<String, String> keyWord, String content) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>(0);
        if (StringUtils.isBlank(content)) {
            logger.warn("The XML content to be parsed is empty.");
            return result;
        }

        Map<String, String> fieldLinkMap = linkList.stream()
                .collect(Collectors.toMap(EsbPointFieldLink::getSourceNameEn, EsbPointFieldLink::getFieldNameEn));
        Map<String, EsbPointFieldLink> linkMap = linkList.stream()
                .collect(Collectors.toMap(EsbPointFieldLink::getFieldNameEn, Function.identity()));
        Map<String, EsbPointField> fieldMap = fieldList.stream()
                .collect(Collectors.toMap(EsbPointField::getFieldNameEn, Function.identity()));
        Map<Integer, List<EsbPointField>> levelFieldMap = fieldList.stream()
                .collect(Collectors.groupingBy(EsbPointField::getFieldLevel));

        Map<String, Object> deduceMap = deduceXPathAndLevel(keyWord, fieldLinkMap, linkMap, fieldMap);
        String xPath = MapUtils.getString(deduceMap, KEY_XPATH);
        int level = MapUtils.getInteger(deduceMap, KEY_LEVEL);
        String flag = "";
        if (level == levelFieldMap.size()) {
            flag = ANALYSE_DIRECTION_TOP;
        } else if (level == 1) {
            flag = ANALYSE_DIRECTION_DOWN;
        } else {
            logger.warn("Parsing from the middle to the sides is not supported.");
            return result;
        }
        logger.info("level:{}, xPath:{}, flag:{}", new Object[] { level, xPath, flag });

        Document document = convertDocument(content);
        try {
            Object obj = getXPath().evaluate(xPath, document, XPathConstants.NODESET);
            NodeList nodes = (NodeList) obj;
            if (null != nodes && nodes.getLength() > 0) {
                for (int j = 0; j < nodes.getLength(); j++) {
                    Node node = nodes.item(j);
                    if (null != node) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        if (ANALYSE_DIRECTION_TOP.equals(flag)) {
                            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(
                                    levelFieldMap.size());
                            list = analyseTop(node, level, linkMap, levelFieldMap, list);
                            Collections.reverse(list);
                            map = listMapToMap(list, 1, map);
                            map = unWrapperData(map);
                        } else if (ANALYSE_DIRECTION_DOWN.equals(flag)) {
                            Map<String, Object> firstMap = analyseSingle(linkMap, levelFieldMap, level, node);
                            firstMap = analyseDown(node, level, linkMap, levelFieldMap, firstMap);
                            map.putAll(firstMap);
                        } else {
                            logger.warn("Parsing from the middle to the sides is not supported.");
                        }
                        result.add(map);
                    }
                }
            }
        } catch (XPathExpressionException e) {
            logger.error(e.getMessage(), e);
        }
        
        if (ANALYSE_DIRECTION_TOP.equals(flag)) {
            Map<String, List<Map<String, Object>>> mergeData = mergeDupliData(result);
            result = mergeResultData(mergeData);
        }
        
        
        
        return result;
    }
    
    /**
     * 合并结果数据
     * @param mergeData  合并重复数据之后的数据
     * @return  合并结果数据
     * @author Monk
     * @date 2021年4月26日 下午4:42:22
     */
    private List<Map<String, Object>> mergeResultData(Map<String, List<Map<String, Object>>> mergeData){
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for(Entry<String, List<Map<String, Object>>> mergeEntry : mergeData.entrySet()) {
            List<Map<String, Object>> objList = mergeEntry.getValue();
            List<Object> tempList = new ArrayList<Object>();
            String nextLevelKey = "";
            Map<String, Object> tempMap = new HashMap<String, Object>();
            for(Map<String, Object> map : objList) {
                for(Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if(value instanceof Map) {
                        nextLevelKey = key;
                        tempList.add(value);
                    }else if(value instanceof List) {
                        nextLevelKey = key;
                        tempList.addAll((List)value);
                    }
                    tempMap.put(key, value);
                }
            }
            tempMap.put(nextLevelKey, tempList);
            result.add(tempMap);
        } 
        return result;
    }
    
    
    /**
     * 合并重复数据
     * @param data  解析XML后的数据
     * @return 合并后的数据
     * @author Monk
     * @date 2021年4月26日 下午4:41:58
     */
    private Map<String, List<Map<String, Object>>> mergeDupliData(List<Map<String, Object>> data) {
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        for(Map<String, Object> map : data) {
            String priKey = MapUtils.getString(map, "PRI_KEY");
            List<Map<String, Object>> tempList = result.get(priKey);
            if(null == tempList) {
                tempList = new ArrayList<Map<String, Object>>();
            }
            tempList.add(map);
            result.put(priKey, tempList);
        }
        return result;
    }

    private Map<String, Object> unWrapperData(Map<String, Object> map) {
        for (Object t : map.values()) {
            map = (Map<String, Object>) t;
            break;
        }
        return map;
    }

    /**
     * 向上解析Node节点的值
     * 
     * @param node
     *            当前节点对象
     * @param level
     *            当前层级
     * @param linkMap
     *            服务源字段和界面展示和字段的关系Map
     * @param levelFieldMap
     *            界面展示的字段信息
     * @param result
     *            解析后的结果
     * @return 解析后的结果
     * @author Monk
     * @date 2021年3月30日 上午10:26:00
     */
    private List<Map<String, Object>> analyseTop(Node node, int level, Map<String, EsbPointFieldLink> linkMap,
            Map<Integer, List<EsbPointField>> levelFieldMap, List<Map<String, Object>> result) {

        if (level <= 0) {
            return result;
        } else {
            Map<String, Object> map = analyseSingle(linkMap, levelFieldMap, level, node);
            String nodeName = getNodeName(node);
            Map<String, Object> currentMap = new HashMap<String, Object>();
            currentMap.put(nodeName, map);
            result.add(currentMap);

            level--;
            node = node.getParentNode().getParentNode();
            analyseTop(node, level, linkMap, levelFieldMap, result);

            return result;
        }
    }

    /**
     * 向上解析Node节点的值
     * 
     * @param node
     *            当前节点对象
     * @param level
     *            当前层级
     * @param linkMap
     *            服务源字段和界面展示和字段的关系Map
     * @param levelFieldMap
     *            界面展示的字段信息
     * @param result
     *            解析后的结果
     * @return 解析后的结果
     * @author Monk
     * @date 2021年3月30日 上午10:26:00
     */
    private Map<String, Object> analyseDown(Node node, int level, Map<String, EsbPointFieldLink> linkMap,
            Map<Integer, List<EsbPointField>> levelFieldMap, Map<String, Object> result) {
        level++;
        if (level > levelFieldMap.size()) {
            return result;
        } else {
            List<Node> nodeList = getChildNodes(node);
            if (nodeList.size() > 0) {
                List<Map<String, Object>> childList = new ArrayList<Map<String, Object>>(0);
                String childNodeName = "";
                for (Node temp : nodeList) {
                    if (StringUtils.isBlank(childNodeName)) {
                        childNodeName = getNodeName(temp);
                    }
                    Map<String, Object> tempMap = analyseSingle(linkMap, levelFieldMap, level, temp);
                    childList.add(tempMap);
                    analyseDown(temp, level, linkMap, levelFieldMap, tempMap);
                }
                result.put(childNodeName, childList);
            }
        }
        return result;
    }

    /**
     * 解析单个XML节点
     * 
     * @param linkMap
     *            服务源字段和界面展示和字段的关系Map
     * @param levelFieldMap
     *            界面展示的字段信息
     * @param level
     *            当前层级
     * @param node
     *            当前节点对象
     * @return 当前节点的值
     * @author Monk
     * @date 2021年3月30日 上午10:24:07
     */
    private Map<String, Object> analyseSingle(Map<String, EsbPointFieldLink> linkMap,
            Map<Integer, List<EsbPointField>> levelFieldMap, int level, Node node) {
        Map<String, Object> result = new HashMap<String, Object>(0);
        if (null == node) {
            return result;
        }
        List<EsbPointField> fields = levelFieldMap.get(level);
        if (CollectionUtils.isNotEmpty(fields)) {
            for (EsbPointField field : fields) {
                String fieldName = field.getFieldNameEn();
                EsbPointFieldLink link = linkMap.get(fieldName);
                if (null == link) {
                    result.put(fieldName, field.getDefaultValue());
                } else {
                    String elementName = link.getSourceNameEn();
                    result.put(elementName, getValue(elementName, node));
                    logger.debug("{}:{}", elementName, getValue(elementName, node));
                }
            }
        }
        return result;
    }

    /**
     * 将List<Map>按照层级转换为Map
     * 
     * @param list
     *            要转换的数据
     * @param level
     *            当前层级
     * @param result
     *            转换后的结果
     * @return 转换后的Map
     * @author Monk
     * @date 2021年4月9日 下午4:00:11
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> listMapToMap(List<Map<String, Object>> list, int level,
            Map<String, Object> result) {
        if (level > list.size()) {
            return result;
        } else {
            Map<String, Object> map = list.get(level - 1);
            result.putAll(map);
            level++;

            for (Object obj : map.values()) {
                Map<String, Object> value = (Map<String, Object>) obj;
                listMapToMap(list, level, value);
            }
            return result;
        }
    }

    /**
     * 获取节点的名称
     * 
     * @param node
     *            节点信息
     * @return 节点名称
     * @author Monk
     * @date 2021年4月9日 下午3:58:22
     */
    private String getNodeName(Node node) {
        String nodeName = node.getNodeName();
        nodeName = StringUtils.isNotBlank(nodeName) && nodeName.indexOf(":") > 0
                ? nodeName.substring(nodeName.indexOf(":") + 1) + NODE_NAME_SUFFIX
                : "";
        return nodeName;
    }

    /**
     * 获取Xml节点的值
     * 
     * @param elemnetName
     *            节点名称
     * @param node
     *            XmlNode对象
     * @return 节点值
     * @author Monk
     * @date 2021年3月27日 下午6:20:02
     */
    private String getValue(String elemnetName, Node node) {
        String value = "";
        try {
            value = (String) getXPath().evaluate(elemnetName, node, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            logger.error(e.getMessage(), e);
        }
        return value;
    }

    /**
     * 推导XPath路径和Xpath路径对应的展示层级
     * 
     * @param keyWord
     *            查询关键字条件及值
     * @param fieldLinkMap
     *            字段映射关系
     * @param linkMap
     *            服务源字段和界面展示和字段的关系Map
     * @param fieldMap
     *            界面展示字段集合
     * @return XPath和Xpath对应的展示层级
     * @author Monk
     * @date 2021年4月9日 下午4:05:17
     */
    private Map<String, Object> deduceXPathAndLevel(Map<String, String> keyWord, Map<String, String> fieldLinkMap,
            Map<String, EsbPointFieldLink> linkMap, Map<String, EsbPointField> fieldMap) {
        Map<String, Object> result = new HashMap<String, Object>();

        TreeMap<Integer, List<EsbPointField>> keyMap = new TreeMap<Integer, List<EsbPointField>>();
        for (Entry<String, String> entry : keyWord.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (StringUtils.isBlank(value)) {
                continue;
            }
            String fieldName = fieldLinkMap.get(key);
            EsbPointField field = fieldMap.get(fieldName);
            int level = field.getFieldLevel();
            List<EsbPointField> keyList = keyMap.get(level);
            if (CollectionUtils.isNotEmpty(keyList)) {
                keyList.add(field);
            } else {
                keyList = new ArrayList<EsbPointField>();
                keyList.add(field);
            }
            keyMap.put(level, keyList);
        }

        Entry<Integer, List<EsbPointField>> entry = keyMap.lastEntry();
        String xPath = buildXPath(keyWord, linkMap, entry.getValue());
        result.put(KEY_LEVEL, entry.getKey());
        result.put(KEY_XPATH, xPath);

        return result;
    }

    /**
     * 构建XPath路径
     * 
     * @param keyWord
     *            查询关键字条件及值
     * @param linkMap
     *            服务源字段和界面展示和字段的关系Map
     * @param conditionList
     *            查询条件
     * @return 完整的XPath路径
     * @author Monk
     * @date 2021年4月9日 下午4:06:45
     */
    private String buildXPath(Map<String, String> keyWord, Map<String, EsbPointFieldLink> linkMap,
            List<EsbPointField> conditionList) {
        StringBuffer xPath = new StringBuffer();
        if (CollectionUtils.isNotEmpty(conditionList)) {
            int num = 0;
            for (int i = 0; i < conditionList.size(); i++) {
                EsbPointField temp = conditionList.get(i);
                String fieldName = temp.getFieldNameEn();
                EsbPointFieldLink link = linkMap.get(fieldName);
                if (null == link) {
                    continue;
                }
                String path = link.getxPath();
                String name = link.getSourceNameEn();
                String value = keyWord.get(name);
                if (StringUtils.isNotBlank(value)) {
                    if (num == 0) {
                        xPath.append(path).append("[");
                    }
                    if (num > 0) {
                        xPath.append(" and ");
                    }
                    xPath.append(name).append("='").append(value).append("'");
                    num++;
                }

                if (num > 0 && i == conditionList.size() - 1) {
                    xPath.append("]");
                }
            }
        }
        return xPath.toString();
    }

    /**
     * 获取Node节点下的子节点
     * 
     * @param node
     *            XmlNode节点
     * @return Node节点下的子节点
     * @author Monk
     * @date 2021年3月27日 下午6:26:43
     */
    private List<Node> getChildNodes(Node node) {
        List<Node> result = new ArrayList<Node>();
        NodeList nodeList = node.getChildNodes();
        if (null != nodeList && nodeList.getLength() > 1) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node temp = nodeList.item(i);
                if (temp.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList tempChildNodeList = temp.getChildNodes();
                    if (null != tempChildNodeList && tempChildNodeList.getLength() > 1) {
                        for (int j = 0; j < tempChildNodeList.getLength(); j++) {
                            Node tempChildNode = tempChildNodeList.item(j);
                            if (tempChildNode.getNodeType() == Node.ELEMENT_NODE) {
                                NodeList nodeList2 = tempChildNode.getChildNodes();
                                if (null != nodeList2 && nodeList2.getLength() > 1) {
                                    result.add(tempChildNode);
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 将xmlStr转换为Document对象
     * 
     * @param content
     *            XmlString字符串
     * @return Document对象
     * @author Monk
     * @date 2021年3月27日 下午5:22:07
     */
    public Document convertDocument(String content) {
        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", true);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", true);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            content = null == content ? "" : content;
            InputStream stream = new ByteArrayInputStream(content.getBytes());
            doc = builder.parse(stream);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return doc;
    }

    /**
     * 获取XPath对象
     * 
     * @return XPath对象
     * @author Monk
     * @date 2021年3月27日 下午6:31:28
     */
    private XPath getXPath() {
        return XPathFactory.newInstance().newXPath();
    }

}

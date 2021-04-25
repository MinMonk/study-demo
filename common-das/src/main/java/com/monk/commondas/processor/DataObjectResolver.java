package com.monk.commondas.processor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.monk.commondas.bean.DataObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据对象解析器
 *
 * @author Monk
 * @version V1.0
 * @date 2020年8月10日 上午11:40:36
 */
public class DataObjectResolver {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(DataObjectResolver.class);

    /**
     * 默认编码集
     */
    private static final String DEFAULT_ENCODE_UTF_8 = "UTF-8";

    /**
     * JAXBContext对象，用于解析数据对象配置XML
     */
    private JAXBContext jaxbContext = null;

    /**
     * 解析数据对象配置
     * 
     * @param configStr
     *            数据对象配置信息
     * @return 数据对象
     * @author Monk
     * @date 2020年8月10日 上午11:42:46
     */
    public DataObject resolveDataObject(String configStr) {
        JAXBContext jaxbContext = getJAXBContext();
        DataObject dataObject = null;
        if (jaxbContext != null && StringUtils.isNotBlank(configStr)) {
            try {
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                InputStream inputStream = new ByteArrayInputStream(configStr.getBytes(DEFAULT_ENCODE_UTF_8));
                dataObject = (DataObject) jaxbUnmarshaller.unmarshal(inputStream);
                if (logger.isDebugEnabled()) {
                    logger.debug("dataObject:[{}]", dataObject);
                }
            } catch (JAXBException | UnsupportedEncodingException e) {
                logger.error("parse data config error.", e);
            }
        } else {
            logger.error("create JAXBContext error.");
        }
        return dataObject;
    }

    /**
     * 获取JAXBContext
     * 
     * @return JAXBContext对象
     * @author houyongchuan
     * @date 2018年5月11日 上午10:12:27
     */
    private JAXBContext getJAXBContext() {
        if (jaxbContext == null) {
            jaxbContext = createJAXBContext();
        }
        return jaxbContext;
    }

    /**
     * 创建JAXBContext 创建InstanceInfo类的JAXBContext，创建就失败返回null
     * 
     * @return JAXBContext对象
     * @author houyongchuan
     * @date 2018年3月4日 上午11:25:05
     */
    private synchronized JAXBContext createJAXBContext() {
        try {
            if (jaxbContext == null) {
                return JAXBContext.newInstance(DataObject.class);
            }
            return jaxbContext;
        } catch (JAXBException e) {
            logger.error("create JAXBContext error:{}", e.getMessage());
        }
        return null;
    }
}

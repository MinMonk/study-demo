package com.monk.common.parsewsdl;

import java.util.Collection;
import java.util.List;

import javax.wsdl.Definition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        String url = args[0];
        String userName = args[1];
        String passwd = args[2];
        if (null == url || url.trim().length() <= 0) {
            logger.error("wsdl url is null.");
            return;
        }
        Definition definition = WsdlUtil.loadWsdl(url, userName, passwd);
        if (null != definition) {
            String namespace = definition.getTargetNamespace();

            logger.info("---namespace:{}", namespace);
            List<WSDLService> serviceList = WsdlUtil.processDefinition(definition, "", "");
            if (null != serviceList && serviceList.size() > 0) {
                for (WSDLService serviceData : serviceList) {
                    if (null != serviceData) {
                        String serviceName = serviceData.getName();
                        logger.info("---serviceName:{}", serviceName);
                        Collection<WSDLPort> ports = serviceData.getPorts();
                        // 验证端点名称
                        for (WSDLPort wsdlPort : ports) {
                            String endpointName = wsdlPort.getName();
                            String addressName = wsdlPort.getSoapAdress();
                            logger.info("---endpointName:{}, addressName:{}", endpointName, addressName);
                        }
                    }
                }
            }
        }
    }
}

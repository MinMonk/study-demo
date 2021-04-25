package com.monk.common.parsewsdl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Input;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.PortType;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.wsdl.extensions.soap12.SOAP12Address;
import javax.wsdl.extensions.soap12.SOAP12Operation;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import org.apache.ws.commons.schema.XmlSchemaCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.wsdl.PortImpl;
import com.ibm.wsdl.ServiceImpl;

public class WsdlUtil {
    private static final Logger logger = LoggerFactory.getLogger(WsdlUtil.class);
    
    /**
     * 判断是否是soap1.1版本
     * 
     * @param soapVersion
     *            soap协议版本
     * @return 判断结果
     * @author huangyulan
     * @date 2020年11月21日 上午11:48:20
     */
    public static boolean isSoap11Protocol(String soapVersion) {
        String protocolStr = "1.1";
        if (isNotEmpty(soapVersion)) {
            if (soapVersion.contains(protocolStr)) {
                return true;
            }
        }
        return false;
    }
    
    public static String getWsdlEnpointInfo(String wsdlUrl, String userName, String passWord) {
        String enPoint = "";
        try {
            Definition definition = loadWsdl(wsdlUrl, userName, passWord);
            if (null != definition) {
                List<WSDLService> serviceList = processDefinition(definition, userName, passWord);
                if (null != serviceList && serviceList.size() > 0) {
                    WSDLService serviceData = serviceList.get(0);
                    Collection<WSDLPort> ports = serviceData.getPorts();
                    if (ports.iterator().hasNext()) {
                        // update 2020-11-21 调整成默认取soap1.1版本的端口
                        for (WSDLPort port : ports) {
                            if (isSoap11Protocol(port.getSoapVersion())) {
                                enPoint = port.getSoapAdress();
                            }
                        }
                        if (!isNotEmpty(enPoint)) {
                            WSDLPort port = ports.iterator().next();
                            if (null != port) {
                                enPoint = port.getSoapAdress();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("[解析带用密码名密码的wsdl出错]" + e.getMessage(), e);
        }
        return enPoint;
    }
    
    public static List<WSDLService> processDefinition(Definition definition, String userName, String passwd) {
        WSDLDefinition wsdlDefinition = new WSDLDefinition();
        List<WSDLService> wsdlServiceList = new ArrayList<WSDLService>();
        try {
            NameSpacePerfix spacePerfix = new NameSpacePerfix();
            XmlSchemaCollection xmlSchemaCollection = null;
            // 命名空间
            String targetNamespace = definition.getTargetNamespace();
            wsdlDefinition.setDocumentBaseURI(definition.getDocumentBaseURI());
            wsdlDefinition.setTargetNamespace(targetNamespace);

            // 初始化xchemaCollection
            xmlSchemaCollection = new XmlSchemaCollection();
            xmlSchemaCollection.setBaseUri(definition.getDocumentBaseURI());
            XSDURIResolver schemaResolver = new XSDURIResolver();
            schemaResolver.setCollectionBaseURI(definition.getDocumentBaseURI());
            schemaResolver.setUserName(userName);
            schemaResolver.setPasswd(passwd);
            xmlSchemaCollection.setSchemaResolver(schemaResolver);
            // 服务
            Map services = definition.getServices();
            if (services != null && services.size() > 0) {
                Iterator iterator = services.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry entry = (Entry) iterator.next();
                    ServiceImpl serviceImpl = (ServiceImpl) entry.getValue();
                    QName nameSpase = (QName) entry.getKey();

                    WSDLService wsdlService = new WSDLService();
                    wsdlService.setName(serviceImpl.getQName().getLocalPart());
                    wsdlService.setQname(nameSpase);
                    wsdlDefinition.addService(nameSpase, wsdlService);

                    // ports
                    Map<?, ?> ports = serviceImpl.getPorts();
                    Iterator<?> itor = ports.entrySet().iterator();
                    while (itor.hasNext()) {
                        Map.Entry<?, ?> map = (Map.Entry<?, ?>) itor.next();
                        String key = (String) map.getKey();
                        PortImpl value = (PortImpl) map.getValue();

                        WSDLPort port = new WSDLPort();
                        port.setName(value.getName());
                        List<ExtensibilityElement> extensibilityElements = value.getExtensibilityElements();
                        for (ExtensibilityElement element : extensibilityElements) {
                            // 协议版本，地址
                            if (element instanceof SOAPAddress) {
                                SOAPAddress soapAddress = (SOAPAddress) element;
                                port.setSoapAdress(soapAddress.getLocationURI());
                                port.setSoapVersion("SOAP 1.1 Protocol");
                            } else if (element instanceof SOAP12Address) {
                                SOAP12Address soap12Address = (SOAP12Address) element;
                                port.setSoapAdress(soap12Address.getLocationURI());
                                port.setSoapVersion("SOAP 1.2 Protocol");
                            }
                        }
                        // port->binding
                        Binding binding = value.getBinding();
                        WSDLBinding wsdlBinding = new WSDLBinding();
                        wsdlBinding.setName(binding.getQName().getLocalPart());
                        wsdlBinding.setQname(binding.getQName());

                        // port->binding->bindingOperation
                        List<BindingOperation> bindingOperations = binding.getBindingOperations();
                        for (BindingOperation bindingOperation : bindingOperations) {
                            WSDLOperation wsdlOperation = new WSDLOperation();
                            List<ExtensibilityElement> bindingExtensibilityElements = bindingOperation
                                    .getExtensibilityElements();
                            for (ExtensibilityElement element : bindingExtensibilityElements) {
                                if (element instanceof SOAPOperation) {
                                    SOAPOperation soapOperation = (SOAPOperation) element;
                                    String soapAction = soapOperation.getSoapActionURI();
                                    String style = soapOperation.getStyle();
                                    wsdlOperation.setSoapAction(soapAction);
                                    wsdlOperation.setStyle(style);
                                } else if (element instanceof SOAP12Operation) {
                                    SOAP12Operation soap12Operation = (SOAP12Operation) element;
                                    String soapAction = soap12Operation.getSoapActionURI();
                                    String style = soap12Operation.getStyle();
                                    Boolean soapActionRequired = soap12Operation.getSoapActionRequired();
                                    wsdlOperation.setSoapAction(soapAction);
                                    wsdlOperation.setStyle(style);
                                    wsdlOperation.setSoapActionRequired(soapActionRequired);
                                }
                            }
                            wsdlOperation.setName(bindingOperation.getName());
                            // port->binding->bindingOperation->Input/Output
                            Input input = bindingOperation.getOperation().getInput();
                            Output output = bindingOperation.getOperation().getOutput();

                            wsdlOperation.setInputMessage(input.getMessage().getQName());
                            wsdlOperation.setOutputMessage(output.getMessage().getQName());
                            wsdlBinding.addOperation(bindingOperation.getName(), wsdlOperation);
                        }
                        // bingding->portType
                        PortType portType = binding.getPortType();
                        WSDLPortType wsdlPortType = new WSDLPortType();
                        wsdlPortType.setName(portType.getQName().getLocalPart());
                        wsdlPortType.setQname(portType.getQName());
                        wsdlBinding.addPortType(portType.getQName(), wsdlPortType);
                        // bingding->portType->Operation
                        List<Operation> operations = portType.getOperations();
                        for (Operation operation : operations) {
                            WSDLOperation wsdlOperation = new WSDLOperation();
                            wsdlOperation.setName(operation.getName());
                            Input input = operation.getInput();
                            Output output = operation.getOutput();
                            wsdlOperation.setInputMessage(input.getMessage().getQName());
                            wsdlOperation.setOutputMessage(output.getMessage().getQName());
                            wsdlPortType.addOperation(operation.getName(), wsdlOperation);
                        }

                        port.addBinding(binding.getQName(), wsdlBinding);
                        wsdlService.addPort(key, port);
                        wsdlServiceList.add(wsdlService);
                    }

                }
            } else {
                logger.error("not found service definition in wsdl");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return wsdlServiceList;

    }

    public static Definition loadWsdl(String wsdlUrl, String userName, String passwd) throws Exception {
        WSDLLocatorImpl wsdlLocator = null;
        logger.info("load wsdl:{}", wsdlUrl);
        WSDLReader reader = getWsdlReader();
        // 读取wsdl文件
        if (wsdlLocator == null) {
            wsdlLocator = new WSDLLocatorImpl(wsdlUrl);
        }
        if (isNotEmpty(userName) && isNotEmpty(passwd)) {
            wsdlLocator.setUserName(userName);
            wsdlLocator.setPasswd(passwd);
        }
        Definition definition = reader.readWSDL(wsdlLocator);

        // 解析WSDL
        if (definition != null) {
            return definition;
        }
        return null;
    }

    /**
     * WSDL Reader 创建
     * 
     * @return
     */
    private static WSDLReader getWsdlReader() {
        WSDLFactory factory = null;
        try {
            factory = WSDLFactory.newInstance();
            WSDLReader reader = factory.newWSDLReader();
            reader.setFeature("javax.wsdl.verbose", true);
            reader.setFeature("javax.wsdl.importDocuments", true);
            return reader;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 是否为空串
     * 
     * @param in
     *            检查字符串
     * @return 字符串是否为空的标识 true or false
     * @author xiaoling
     * @date 2018年5月21日 下午1:55:00
     */
    public static boolean isNotEmpty(String in) {
        return in != null && !"".equals(in.trim());
    }

}

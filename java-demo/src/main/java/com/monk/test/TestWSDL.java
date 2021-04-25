package com.monk.test;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.predic8.schema.creator.AbstractSchemaCreator;
import com.predic8.schema.creator.DummySchemaCreator;
import com.predic8.wadl.creator.JsonCreator;
import com.predic8.wsdl.Binding;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.Port;
import com.predic8.wsdl.PortType;
import com.predic8.wsdl.Service;
import com.predic8.wsdl.WSDLParser;
import com.predic8.wstool.creator.FormCreator;
import com.predic8.wstool.creator.RequestCreator;
import com.predic8.wstool.creator.RequestTemplateCreator;
import com.predic8.wstool.creator.SOARequestCreator;

import groovy.xml.MarkupBuilder;

/**
 * API地址：http://www.membrane-soa.org/soa-model/api-doc/1.4/groovydoc/index.html
 *
 * @author Monk
 * @version V1.0
 * @date 2020年9月11日 下午6:27:17
 */
public class TestWSDL {
    
    private static final Logger logger = LoggerFactory.getLogger(TestWSDL.class);
    
    public static void main(String[] args) {
        String wsdlUrl = "http://10.204.105.129:8011/MDM/OSB_BP_MDM_HQ_PageInquiryVendorHeaderSrv.v0/proxy/OSB_BP_MDM_HQ_PageInquiryVendorHeaderSrv?wsdl";
//        wsdlUrl = "http://10.204.105.129:9011/DAS/services/CommonDAS?wsdl";
        wsdlUrl = "D:\\test\\OSB_SCM_SCM_HQ_ImportWarehouseInfoSrv.wsdl";
        test1(wsdlUrl);
        
        System.out.println(TimeUnit.HOURS.toMillis(0));
    }


    /**
     * @param wsdlUrl
     * @author Monk
     * @date 2020年9月11日 下午6:00:17
     */
    private static void test1(String wsdlUrl) {
        WSDLParser parser = new WSDLParser();
        Definitions wsdl = parser.parse(wsdlUrl);
        StringWriter writer = new StringWriter();
        SOARequestCreator creator = new SOARequestCreator(wsdl, new RequestTemplateCreator(), new MarkupBuilder(writer));

        for (Service service : wsdl.getServices()) {
            for (Port port : service.getPorts()) {
                Binding binding = port.getBinding();
                PortType portType = binding.getPortType();
                for (Operation op : portType.getOperations()) {
                    logger.info("portName:{}, opName:{}, bindingName:{}", new Object[] {port.getName(), op.getName(), binding.getName()});
                    creator.createRequest(port.getName(), op.getName(), binding.getName());
                    System.out.println(writer);
                    writer.getBuffer().setLength(0);
                }
            }
        }
    }
}
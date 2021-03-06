
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package com.monk.commondas;

import com.monk.commondas.constant.DasConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monk.commondas.common.BIZErrorCode;
import com.monk.commondas.processor.AbstractDASProcessor;
import com.monk.commondas.processor.DASProcessorFactory;

/**
 * This class was generated by Apache CXF 3.0.2 2020-08-06T16:01:20.968+08:00
 * Generated source version: 3.0.2
 * 
 */

@javax.jws.WebService(serviceName = "CommonDAS", portName = "CommonDASPort", targetNamespace = "http://monk.com/CommonDAS", wsdlLocation = "file:./CommonDAS.wsdl", endpointInterface = "com.monk.commondas.CommonDAS")

public class CommonDASPortImpl implements CommonDAS {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(CommonDASPortImpl.class);

    @Override
    public OutputParameters process(InputParameters payload) {
        OutputParameters _return = new OutputParameters();

        String dataConfig = payload.getDATACONFIG();
        String configVersion = payload.getCONFIGVERSION();
        String serviceNameEn = payload.getSERVICENAMEEN();
        String majorVersion = payload.getMAJORVERSION();
        String inputJson = payload.getINPUTJSON();
        
        logger.info("dataConfigStr:[{}]", dataConfig);
        AbstractDASProcessor processor = null;
        try {
            processor = DASProcessorFactory.createSQLGenerator(dataConfig, configVersion,
                    serviceNameEn, majorVersion);
            _return = processor.process(inputJson);
        } catch (Exception e) {
           // _return.setESBFLAG(DasConstant.SUCCESS_FLAG);
            _return.setBIZSERVICEFLAG(DasConstant.FAILED_FLAG);
            _return.setBIZRETURNCODE(BIZErrorCode.BIZ_ERROR.getCode());
            _return.setBIZRETURNMESSAGE(BIZErrorCode.BIZ_ERROR.getMsg() + ". " + e.getMessage());
            return _return;
        }

        return _return;
    }

}

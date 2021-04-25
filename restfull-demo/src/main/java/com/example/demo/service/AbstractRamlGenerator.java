package com.example.demo.service;

import com.example.demo.entity.EsbService;
import com.example.demo.entity.EsbServiceParameter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.ServiceMode;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AbstractRamlGenerator {
    private static final Logger logger = LoggerFactory.getLogger(AbstractRamlGenerator.class);

    protected static final String IS_NULL_FLAG = "Y";
    protected static final Long ROOT_ELEMENT = -1L;
    protected static final String NEXTLINE = "\r\n";
    protected static final String SPACE = "  ";
    protected static final String[] SUPPORT_METHODS = new String[] { "get", "post"};

    @Autowired
    private ServiceRepository serviceRepository;

    public void generateRaml(EsbService serviceInfo) {
        StringBuilder builder = new StringBuilder();
        if (null != serviceInfo) {
            builder.append(generateHeader(serviceInfo));

            builder.append(generatePathParameterPart(serviceInfo, 0));
            builder.append(generateParameterPart(serviceInfo, 1, ParameterPosition.INPUT_PATH));
            for(String method: SUPPORT_METHODS){
                builder.append(getLevelSpace(1)).append(method).append(":").append(NEXTLINE);
                builder.append(generateParameterPart(serviceInfo, 2, ParameterPosition.INPUT_URL));
                builder.append(generateParameterPart(serviceInfo, 2, ParameterPosition.INPUT_HEADER));
                builder.append(generateParameterPart(serviceInfo, 2, ParameterPosition.INPUT_BODY));
                builder.append(generateResponsePart(serviceInfo, 2));
            }

            //builder.append(generateResponsePart(serviceInfo, 2));


            // 生成RAML文件
            String serviceNameEn = serviceInfo.getServiceNameEn();
            String path = getDescDir(serviceNameEn);
            logger.info("path : " + path);
            try {
                FileUtils.writeStringToFile(new File(path), builder.toString(), "UTF-8");
            } catch (IOException e) {
                logger.error("Generate RAML file failed. " + e.getMessage());
            }
        }
    }

    protected String generatePathParameterPart(EsbService serviceInfo, int startLevel){
        StringBuilder path = new StringBuilder();
        List<EsbServiceParameter> serviceParameters = serviceRepository.queryServiceParams(serviceInfo.getId(), ParameterPosition.INPUT_PATH.toString());
        for(EsbServiceParameter parameter : serviceParameters){
            path.append(getLevelSpace(startLevel)).append("/{").append(parameter.getParameterNameEn()).append("}");
        }
        return path.append(":").append(NEXTLINE).toString();
    }

    protected String generateParameterPart(EsbService serviceInfo, int startLevel, ParameterPosition position){
        StringBuilder paramStr = new StringBuilder();
        logger.info("name:{}, value:{}", position.toString(), position.getValue());
        List<EsbServiceParameter> serviceParameters = serviceRepository.queryServiceParams(serviceInfo.getId(), position.toString());
        if(!CollectionUtils.isEmpty(serviceParameters)){
            paramStr.append(getLevelSpace(startLevel)).append(position.getValue()).append(":").append(NEXTLINE);
            for(EsbServiceParameter parameter : serviceParameters){
//                paramStr.append(getLevelSpace(startLevel + 1)).append(parameter.getParameterNameEn()).append(":").append(NEXTLINE);
//                paramStr.append(getLevelSpace(startLevel + 2)).append("type: ").append(getMapping(parameter.getDataType())).append(NEXTLINE);
//                paramStr.append(getLevelSpace(startLevel + 2)).append("required: ").append(getRequired(parameter.getIsNullFlag())).append(NEXTLINE);
//                paramStr.append(getLevelSpace(startLevel + 2)).append("description: ").append(parameter.getParameterNameCh()).append(NEXTLINE);
//                paramStr.append(getLevelSpace(startLevel + 2)).append("example: ").append(parameter.getExample()).append(NEXTLINE);
                paramStr.append(generateFields(parameter, startLevel + 1));
            }
        }
        return paramStr.toString();
    }

    protected String generateResponsePart(EsbService serviceInfo, int startLevel){
        StringBuilder builder = new StringBuilder();
        List<EsbServiceParameter> errCodes = serviceRepository.queryServiceParams(serviceInfo.getId(), ParameterPosition.ERROR_CODE.getValue());
        if(!CollectionUtils.isEmpty(errCodes)){
            builder.append(getLevelSpace(startLevel)).append("responses:").append(NEXTLINE);
            for(EsbServiceParameter parameter : errCodes){
                builder.append(getLevelSpace(startLevel + 1)).append(parameter.getParameterNameEn()).append(":").append(NEXTLINE);
                builder.append(getLevelSpace(startLevel + 2)).append("body:").append(NEXTLINE);
				// 判断响应MediaType是否为空，如果不为空，就需要拼接上响应的MediaType，并且缩进的空格等级要+1
				// 待补充
				
                builder.append(getLevelSpace(startLevel + 3)).append("type: string").append(NEXTLINE);

                List<EsbServiceParameter> examples = serviceRepository.queryServiceParams(serviceInfo.getId(), ParameterPosition.EXAMPLE_VALUE.getValue());
                if(!CollectionUtils.isEmpty(examples)){
                    builder.append(getLevelSpace(startLevel + 3)).append("examples:").append(NEXTLINE);
                    for(EsbServiceParameter example : examples){
                        builder.append(getLevelSpace(startLevel + 4)).append(example.getParameterNameEn()).append(":").append(NEXTLINE);
                        builder.append(getLevelSpace(startLevel + 5)).append(example.getExample()).append(NEXTLINE);
                    }
                }
            }
        }
        return builder.toString();
    }

    /**
     * 生成RAML设计文档的头部部分
     *
     * @param serviceInfo
     *            服务信息
     * @return RAML设计文档的头部部分
     * @author Monk
     * @date 2020年3月12日 下午10:20:40
     */
    private String generateHeader(EsbService serviceInfo) {
        StringBuilder builder = new StringBuilder();
        builder.append("#%RAML 1.0").append(NEXTLINE);
        builder.append("title: ").append(serviceInfo.getServiceNameCN()).append(NEXTLINE);
        builder.append("version: ").append(serviceInfo.getStandardVersion()).append(NEXTLINE);
        builder.append("mediaType: ").append(MediaType.APPLICATION_JSON).append(NEXTLINE);
        String localHostIp = "";
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            localHostIp =  localhost.getHostAddress();
        } catch (UnknownHostException e) {
            localHostIp = "localhost";
            logger.error("Get local host ip failed" + e.getMessage(), e);
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        int port = request.getServerPort();
        builder.append("baseUri: http://"+ localHostIp + ":" + port).append("/v" + serviceInfo.getMajorVersion()).append(NEXTLINE);
        return builder.toString();
    }


    /**
     * 获取生成的RAML文件路径
     *
     * @param serviceName
     *            服务英文名
     * @return 生成的RAML文件路径
     * @author Monk
     * @date 2020年3月12日 下午11:03:35
     */
    protected String getDescDir(String serviceName) {
        //String basedir = sysConfigDS.getConfigValueByName(Constants.SYSTEM_FILE_PATH);
        String basedir = "D:\\testDir";
        if (!basedir.endsWith("\\") && !basedir.endsWith("/")) {
            basedir += File.separator;
        }
        StringBuffer descDir = new StringBuffer();
        descDir.append(basedir)
                //.append(System.currentTimeMillis())
                .append(File.separator).append(serviceName)
                .append(File.separator).append(serviceName).append(".raml");
        return descDir.toString();
    }

    /**
     * 拼接普通字段参数信息
     *
     * @param parameter
     *            当前节点参数信息
     * @param level
     *            缩进等级
     * @return 拼接后的普通字段参数信息
     * @author Monk
     * @date 2020年3月12日 下午10:25:24
     */
    protected String generateFields(EsbServiceParameter parameter, int level) {
        StringBuilder builder = new StringBuilder();
        String dataType = parameter.getDataType();
        String nullFlag = parameter.getIsNullFlag();
        String nameEn = parameter.getParameterNameEn();
        String nameCn = parameter.getParameterNameCh();
        String exampleValue = parameter.getExample();
        builder.append(getLevelSpace(level)).append(nameEn + ": ").append(NEXTLINE);
        level++;
        builder.append(getLevelSpace(level)).append("type: ").append(getMapping(dataType)).append(NEXTLINE);
        builder.append(getLevelSpace(level)).append("required: ").append(getRequired(nullFlag)).append(NEXTLINE);
        builder.append(getLevelSpace(level)).append("description: ").append(nameCn).append(NEXTLINE);
        builder.append(getLevelSpace(level)).append("example: ").append(exampleValue).append(NEXTLINE);
        return builder.toString();
    }

    /**
     * 获取缩进距离
     *
     * @param level
     *            缩进级别
     * @return 缩进距离
     * @author Monk
     * @date 2020年3月12日 下午10:30:37
     */
    protected String getLevelSpace(int level) {
        String space = "";
        for (int i = 0; i < level; i++) {
            space += SPACE;
        }
        return space;
    }

    /**
     * 参数映射转换
     *
     * @param dataType
     *            参数类型
     * @return 转换后的参数类型
     * @author Monk
     * @date 2020年3月12日 下午10:31:03
     */
    protected String getMapping(String dataType) {
        Map<String, String> dataMapping = new HashMap<String, String>();
        dataMapping.put("NUMBER", "number");
        dataMapping.put("VARCHAR2", "string");
        dataMapping.put("ENTITY", "object");
        dataMapping.put("COLLECTION", "array");
        dataMapping.put("DATE", "datetime-only");
        String mappingType = dataMapping.get(dataType);
        mappingType = StringUtils.isBlank(mappingType) ? "string" : mappingType;
        return mappingType;
    }

    /**
     * 判断属性是否不能为空
     *
     * @param isNull
     *            字段属性
     * @return 返回字段属性是否为空
     * @author Monk
     * @date 2020年3月12日 下午10:31:23
     */
    protected String getRequired(String isNull) {
        return StringUtils.isBlank(isNull) || IS_NULL_FLAG.equals(isNull) ? "false" : "true";
    }


}

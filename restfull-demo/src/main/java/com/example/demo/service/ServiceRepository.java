package com.example.demo.service;

import com.alibaba.fastjson.JSON;
import com.example.demo.entity.EsbService;
import com.example.demo.entity.EsbServiceParameter;
import org.apache.commons.collections.MapUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class ServiceRepository {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ServiceRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public EsbService queryService(Long serviceId){
        Map<String, Object> queryResult = jdbcTemplate.queryForMap("SELECT * FROM ESB_SERVICE WHERE ID = ?", serviceId);
        logger.info("Method queryService result:[{}]", JSON.toJSONString(queryResult));
        EsbService service = new EsbService();
        service.setId(MapUtils.getLong(queryResult, "ID"));
        service.setServiceNameEn(MapUtils.getString(queryResult, "SERVICE_NAME_EN"));
        service.setServiceNameCN(MapUtils.getString(queryResult, "SERVICE_NAME_CN"));
        service.setMajorVersion(MapUtils.getString(queryResult, "MAJOR_VERSION"));
        service.setStandardVersion(MapUtils.getString(queryResult, "STANDARD_VERSION"));
        return service;
    }

    public List<EsbServiceParameter> queryServiceParams(Long serviceId, String type){
        List<?> queryResult = jdbcTemplate.queryForList("SELECT * FROM ESB_SERVICE_PARAMETER WHERE ENABLED_FLAG = 'Y' AND SERVICE_ID = ? and PARAMETER_TYPE = ? ORDER BY PARAMETER_SEQUENCE", new Object[]{serviceId, type});
        logger.info("Method queryServiceParams result:[{}]", JSON.toJSONString(queryResult));
        List<EsbServiceParameter> result = new ArrayList<EsbServiceParameter>();

        for(Object obj : queryResult){
            EsbServiceParameter serviceParameter = new EsbServiceParameter();
            serviceParameter.setId(MapUtils.getLong((Map) obj, "ID"));
            serviceParameter.setServiceId(MapUtils.getLong((Map) obj, "SERVICE_ID"));
            serviceParameter.setParameterParentId(MapUtils.getLong((Map) obj, "PARAMETER_PARENT_ID"));
            serviceParameter.setParameterNameCh(MapUtils.getString((Map) obj, "PARAMETER_NAME_CH"));
            serviceParameter.setParameterNameEn(MapUtils.getString((Map) obj, "PARAMETER_NAME_EN"));
            serviceParameter.setDataType(MapUtils.getString((Map) obj, "DATA_TYPE"));
            serviceParameter.setIsNullFlag(MapUtils.getString((Map) obj, "ISNULL_FLAG"));
            serviceParameter.setParameterType(MapUtils.getString((Map) obj, "PARAMETER_TYPE"));
            serviceParameter.setParameterSequence(MapUtils.getLong((Map) obj, "PARAMETER_SEQUENCE"));
            serviceParameter.setRemark(MapUtils.getString((Map) obj, "REMARK"));
            serviceParameter.setExample(MapUtils.getString((Map) obj, "EXAMPLE"));
            serviceParameter.setDefaultValue(MapUtils.getString((Map) obj, "DEFAULT_VALUE"));
            result.add(serviceParameter);
        }

        return result;
    }
}

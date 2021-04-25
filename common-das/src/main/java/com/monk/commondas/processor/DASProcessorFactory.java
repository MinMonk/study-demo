package com.monk.commondas.processor;

import com.monk.commondas.constant.DbAdapterServiceType;

public class DASProcessorFactory {

    /**
     * 创建SQL生成器
     * 
     * @param configStr
     *            配置信息
     * @param configVersion
     *            配置信息版本
     * @param serviceNameEn
     *            服务英文名
     * @param majorVersion
     *            服务主版本
     * @return SQL生成器
     * @author Monk
     * @date 2020年8月10日 下午2:50:19
     */
    public static AbstractDASProcessor createSQLGenerator(String configStr, String configVersion,
            String serviceNameEn, String majorVersion) {
        DbAdapterServiceType operationType = getAdapterType(serviceNameEn);
        if (DbAdapterServiceType.INQUIRY.equals(operationType)) {
            return new InquiryProcessor(configStr, configVersion, serviceNameEn, majorVersion);
        } else if (DbAdapterServiceType.DELETE.equals(operationType)) {
            return new DeleteProcessor(configStr, configVersion, serviceNameEn, majorVersion);
        } else if (DbAdapterServiceType.IMPORT.equals(operationType)) {
            return new ImportProcessor(configStr, configVersion, serviceNameEn, majorVersion);
        } else {
            throw new RuntimeException("Unknown operation type." + operationType);
        }
    }

    /**
     * 根据服务名称获取适配器的操作类型
     * 
     * @param serviceNameEn
     *            服务名称
     * @return 适配器的操作类型
     * @author Monk
     * @date 2020年8月10日 下午2:50:55
     */
    private static DbAdapterServiceType getAdapterType(String serviceNameEn) {
        if (serviceNameEn.startsWith(DbAdapterServiceType.INQUIRY.getTypeEn())) {
            return DbAdapterServiceType.INQUIRY;
        } else if (serviceNameEn.startsWith(DbAdapterServiceType.DELETE.getTypeEn())) {
            return DbAdapterServiceType.DELETE;
        } else if (serviceNameEn.startsWith(DbAdapterServiceType.IMPORT.getTypeEn())) {
            return DbAdapterServiceType.IMPORT;
        } else {
            throw new RuntimeException("Unknown operation type.");
        }
    }
}

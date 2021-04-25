package com.monk.commondas.constant;

import com.monk.commondas.exception.ValidateException;

/**
 * DB适配服务类型
 *
 * @author Monk
 * @version V1.0
 * @date 2020年7月24日 下午6:48:49
 */
public enum DbAdapterServiceType {
    /**
     * 查询服务
     */
    INQUIRY("Inquiry", "查询"),
    /**
     * 导入服务
     */
    IMPORT("Import", "导入"),
    /**
     * 删除服务
     */
    DELETE("Delete", "删除");
    
    /**
     * 中文名
     */
    private String typeCn;
    
    /**
     * 英文名
     */
    private String typeEn;
    
    private DbAdapterServiceType(String typeEn, String typeCn) {
        this.typeEn = typeEn;
        this.typeCn = typeCn;
    }
    
    public String getTypeCn() {
        return typeCn;
    }
    
    public String getTypeEn() {
        return typeEn;
    }
    
    public static DbAdapterServiceType getServiceType(String type) {
        if(INQUIRY.toString().equalsIgnoreCase(type)) {
            return INQUIRY;
        }else if(IMPORT.toString().equalsIgnoreCase(type)) {
            return IMPORT;
        }else if(DELETE.toString().equalsIgnoreCase(type)) {
            return DELETE;
        }else {
            throw new ValidateException("Unknown database adapter service type.");
        }
    }

}

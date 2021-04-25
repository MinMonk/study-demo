/**
 * 
 * 文件名：String2JsonDemo.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.demo;

import java.io.File;

import org.apache.commons.io.FileUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年1月25日 下午6:22:06
 */
public class String2JsonDemo {
    
    /**
     * <p>
     * 
     * {
  "ESB_FLAG": "TRUE",
  "ESB_RETURN_CODE": null,
  "ESB_RETURN_MESSAGE": null,
  "BIZ_SERVICE_FLAG": "TRUE",
  "BIZ_RETURN_CODE": null,
  "BIZ_RETURN_MESSAGE": "查询成功",
  "INSTANCE_ID": "rs-95975b35-69df-447d-abd4-4aad13bbc8e9",
  "TOTAL_RECORD": 1,
  "TOTAL_PAGE": 1,
  "PAGE_SIZE": 500,
  "CURRENT_PAGE": 1,
  "OUTPUTCOLLECTION": {
    "OUTPUTCOLLECTION_ITEM": [
      {
        "MATERIAL_CODE": "10489654",
        "SHORT_NAME": "IBCF/TrGW\\中兴\\ETCA\\软件\\IBCF/TrGW基本软件包(每网元)",
        "LONG_NAME": "IMS网间互联互通设备（IBCF/TrGW）\\中兴\\ETCA\\软件\\IBCF/TrGW基本软件包(每网元)",
        "MATERIAL_UNIT": "套",
        "CATALOG1_NUM": "31",
        "CATALOG1_DESC": "通信网络设备",
        "CATALOG2_NUM": "3112",
        "CATALOG2_DESC": "核心网设备",
        "CATALOG3_NUM": "311202",
        "CATALOG3_DESC": "IMS核心网",
        "PRODUCT_NUM": "311202008",
        "PRODUCT_DESC": "IMS网间互联互通设备（IBCF/TrGW）",
        "SUB_PRODUCT_NUM": "311202008",
        "SUB_PRODUCT_DESC": "IMS网间互联互通设备（IBCF/TrGW）",
        "APPLICANT": "zhangsan@163.com",
        "APPLY_DATE": "2020-11-25T00:00:00",
        "APPLY_COMPANY": "HQ",
        "BOM_FLAG": null,
        "ASSEMBLY_INFO": null,
        "ERP_MATE_TMP_NAME": "库存资产型物料模版",
        "AREA_FLAG": null,
        "STOCK_FLAG": null,
        "ITEM_TYPE": "FA明细物料",
        "TAX_CODE": "VAT13",
        "VAT_ADD_RATE": "0.000",
        "FEE_ACCOUNT": null,
        "STATUS": "有效",
        "PROVINCE_CODE": "AH",
        "LAST_UPDATE_DATE": "2020-11-25T15:30:10",
        "REFERENCE1": null,
        "REFERENCE2": null,
        "REFERENCE3": null,
        "REFERENCE4": null,
        "REFERENCE5": null,
        "REFERENCE6": null,
        "REFERENCE7": null,
        "REFERENCE8": null,
        "REFERENCE9": null,
        "REFERENCE10": null,
        "OUTPUT_EXT": null
      }
    ]
  }
}
     * 
     * </p>
     */
    
    public static void main(String[] args) throws Exception {
        String fillPath = "D:\\testDir\\json_response.txt";
        String content = FileUtils.readFileToString(new File(fillPath));
        
        JSONObject json = JSONObject.fromObject(content);
        String path = "OUTPUTCOLLECTION.OUTPUTCOLLECTION_ITEM[30].LONG_NAME";
        Object obj = Xml2JsonDemo.getValue(json, path);
        System.out.println(obj);
        
    }

}

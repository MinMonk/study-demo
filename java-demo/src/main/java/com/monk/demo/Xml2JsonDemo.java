/**
 * 
 * 文件名：Xml2JsonDemo.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.demo;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年1月25日 下午4:41:15
 */
public class Xml2JsonDemo {

    /**
     * <p>
     * <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
    <soapenv:Header/>
    <soap:Body xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
        <ns2:OutputParameters xmlns:ns2="http://soa.monk.com/OSB_BP_MDM_HQ_PageInquiryVendorSrv" xmlns="http://soa.monk.com/MsgHeader">
            <ns2:ESB_FLAG>TRUE</ns2:ESB_FLAG>
            <ns2:ESB_RETURN_CODE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
            <ns2:ESB_RETURN_MESSAGE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
            <ns2:BIZ_SERVICE_FLAG>TRUE</ns2:BIZ_SERVICE_FLAG>
            <ns2:BIZ_RETURN_CODE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
            <ns2:BIZ_RETURN_MESSAGE>查询成功</ns2:BIZ_RETURN_MESSAGE>
            <ns2:INSTANCE_ID>c8349ba0-54f9-4a48-9505-e069a415981f</ns2:INSTANCE_ID>
            <ns2:TOTAL_RECORD>32</ns2:TOTAL_RECORD>
            <ns2:TOTAL_PAGE>1</ns2:TOTAL_PAGE>
            <ns2:PAGE_SIZE>100</ns2:PAGE_SIZE>
            <ns2:CURRENT_PAGE>1</ns2:CURRENT_PAGE>
            <ns2:OUTPUTCOLLECTION>
                <ns2:OUTPUTCOLLECTION_ITEM>
                    <ns2:VENDOR_ID>106393840</ns2:VENDOR_ID>
                    <ns2:VENDOR_NAME>长沙永讯科技有限公司0</ns2:VENDOR_NAME>
                    <ns2:MD_CODE>MDM_106393840</ns2:MD_CODE>
                    <ns2:CERTIFICATE_FLAG>Y</ns2:CERTIFICATE_FLAG>
                    <ns2:ORGCERT_NUMBER>77676520-3</ns2:ORGCERT_NUMBER>
                    <ns2:IDENTIFY_CODE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:SOC_IDEN>914301027767652039</ns2:SOC_IDEN>
                    <ns2:CREATION_DATE>2018-09-06T17:24:38</ns2:CREATION_DATE>
                    <ns2:VAT_FLAG>YBNSR</ns2:VAT_FLAG>
                    <ns2:TAX_REGISTRATION_NUMBER xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:STOCK_FLAG>N</ns2:STOCK_FLAG>
                    <ns2:INNER_FLAG>QW</ns2:INNER_FLAG>
                    <ns2:INNER_CODE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:TRADING_PARTY>N</ns2:TRADING_PARTY>
                    <ns2:CUSTOMER_NUMBER xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:CORPORATION_TYPE>QYFRLX1</ns2:CORPORATION_TYPE>
                    <ns2:LAST_UPDATE_DATE>2021-01-21T15:05:55</ns2:LAST_UPDATE_DATE>
                    <ns2:VENDOR_STATUS>Y</ns2:VENDOR_STATUS>
                    <ns2:VENDOR_OLD_NAME xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:LEGAL_REPRESENTATIVE>曾小林</ns2:LEGAL_REPRESENTATIVE>
                    <ns2:TAX_REG_ADDRESS xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:OFFICE_ADDRESS xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:REGISTRY_ADDRESS>湖南省长沙市芙蓉区万家丽中路一段166号东郡华城广场1115房</ns2:REGISTRY_ADDRESS>
                    <ns2:BUSINESS_RANGE>安防设备、计算机软硬件、五金交电、办公用品、化工产品（不含危险品、监控品）、仪器仪表、电子产品、通讯器材及其配套产品、有线电视设备的研发、销售；通信工程、防雷工程、网络工程的设计；电信业务代理（不含增值电信业务）。</ns2:BUSINESS_RANGE>
                    <ns2:COMPANY_TYPE>GSLX_01</ns2:COMPANY_TYPE>
                    <ns2:BUSINESS_TIMELIMIT xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:REGISTRY_FUND>5050</ns2:REGISTRY_FUND>
                    <ns2:REGISTRY_CURRENCY>CNY</ns2:REGISTRY_CURRENCY>
                    <ns2:COUNTRY xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:STATE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:CITY xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:SOURCE_CODE>ES</ns2:SOURCE_CODE>
                    <ns2:VIRTUAL_VENDOR>N</ns2:VIRTUAL_VENDOR>
                    <ns2:VIRTUAL_TYPE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:VENDOR_TYPE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:OUTPUT_EXT xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:VENDOR_COMPANY_INFO>
                        <ns2:VENDOR_COMPANY_INFO_ITEM>
                            <ns2:VENDOR_COMPANT_ID>62337129</ns2:VENDOR_COMPANT_ID>
                            <ns2:VENDOR_ID>106393840</ns2:VENDOR_ID>
                            <ns2:ORG_NAME>OU_CMTT_311010_中移铁通本部</ns2:ORG_NAME>
                            <ns2:OU_CODE>311010</ns2:OU_CODE>
                            <ns2:VALID_FLAG>Y</ns2:VALID_FLAG>
                            <ns2:CREATION_DATE>2018-10-31T09:30:46</ns2:CREATION_DATE>
                            <ns2:EXPIRY_DATE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                            <ns2:PURCHASE_TYPE>Y</ns2:PURCHASE_TYPE>
                            <ns2:REMARK xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                            <ns2:VENDOR_ACCOUNT_INFO>
                                <ns2:VENDOR_ACCOUNT_INFO_ITEM>
                                    <ns2:VENDOR_COMPANT_ID>62337129</ns2:VENDOR_COMPANT_ID>
                                    <ns2:VENDOR_ACCOUNT_ID>7447487</ns2:VENDOR_ACCOUNT_ID>
                                    <ns2:VENDOR_ID>106393840</ns2:VENDOR_ID>
                                    <ns2:MAIN_ACCOUNT_FLAG>Y</ns2:MAIN_ACCOUNT_FLAG>
                                    <ns2:BANK_NAME>中国建设银行</ns2:BANK_NAME>
                                    <ns2:BANK_CODE>105</ns2:BANK_CODE>
                                    <ns2:BRANCH_BANK>中国建设银行股份有限公司长沙铁银支行</ns2:BRANCH_BANK>
                                    <ns2:BRANCH_CODE>105551003055</ns2:BRANCH_CODE>
                                    <ns2:CNAP_NUMBER>105551003055</ns2:CNAP_NUMBER>
                                    <ns2:ACCOUNT_NAME>长沙永讯科技有限公司</ns2:ACCOUNT_NAME>
                                    <ns2:ACCOUNT_NUMBER>43001710661052500120</ns2:ACCOUNT_NUMBER>
                                    <ns2:ACCOUNT_CURRENCY>CNY</ns2:ACCOUNT_CURRENCY>
                                    <ns2:VALID_FLAG>Y</ns2:VALID_FLAG>
                                    <ns2:OUTPUT_EXT>{"SPECIAL_ACCOUNT_FLAG":"N"}</ns2:OUTPUT_EXT>
                                </ns2:VENDOR_ACCOUNT_INFO_ITEM>
                            </ns2:VENDOR_ACCOUNT_INFO>
                            <ns2:VENDOR_CONTACTS_INFO>
                                <ns2:VENDOR_CONTACTS_INFO_ITEM>
                                    <ns2:VENDOR_COMPANT_ID>62337129</ns2:VENDOR_COMPANT_ID>
                                    <ns2:VENDOR_ID>106393840</ns2:VENDOR_ID>
                                    <ns2:VENDOR_CONTACTS_ID>1814217</ns2:VENDOR_CONTACTS_ID>
                                    <ns2:CONTACT_NAME>雷成华</ns2:CONTACT_NAME>
                                    <ns2:CONTACT_CELLPHONE>13974927627</ns2:CONTACT_CELLPHONE>
                                    <ns2:CONTACT_PHONE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                                    <ns2:CONTACT_FAX xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                                    <ns2:CONTACT_MAIL>13974927627@139.com</ns2:CONTACT_MAIL>
                                    <ns2:OUTPUT_EXT xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                                </ns2:VENDOR_CONTACTS_INFO_ITEM>
                            </ns2:VENDOR_CONTACTS_INFO>
                            <ns2:VENDOR_BUSINESS_INFO/>
                            <ns2:OUTPUT_EXT xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                        </ns2:VENDOR_COMPANY_INFO_ITEM>
                    </ns2:VENDOR_COMPANY_INFO>
                    <ns2:VENDOR_ATTA_INFO/>
                </ns2:OUTPUTCOLLECTION_ITEM>
                <ns2:OUTPUTCOLLECTION_ITEM>
                    <ns2:VENDOR_ID>106393840</ns2:VENDOR_ID>
                    <ns2:VENDOR_NAME>长沙永讯科技有限公司1</ns2:VENDOR_NAME>
                    <ns2:MD_CODE>MDM_106393840</ns2:MD_CODE>
                    <ns2:CERTIFICATE_FLAG>Y</ns2:CERTIFICATE_FLAG>
                    <ns2:ORGCERT_NUMBER>77676520-3</ns2:ORGCERT_NUMBER>
                    <ns2:IDENTIFY_CODE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:SOC_IDEN>914301027767652039</ns2:SOC_IDEN>
                    <ns2:CREATION_DATE>2018-09-06T17:24:38</ns2:CREATION_DATE>
                    <ns2:VAT_FLAG>YBNSR</ns2:VAT_FLAG>
                    <ns2:TAX_REGISTRATION_NUMBER xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:STOCK_FLAG>N</ns2:STOCK_FLAG>
                    <ns2:INNER_FLAG>QW</ns2:INNER_FLAG>
                    <ns2:INNER_CODE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:TRADING_PARTY>N</ns2:TRADING_PARTY>
                    <ns2:CUSTOMER_NUMBER xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:CORPORATION_TYPE>QYFRLX1</ns2:CORPORATION_TYPE>
                    <ns2:LAST_UPDATE_DATE>2021-01-21T15:05:55</ns2:LAST_UPDATE_DATE>
                    <ns2:VENDOR_STATUS>Y</ns2:VENDOR_STATUS>
                    <ns2:VENDOR_OLD_NAME xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:LEGAL_REPRESENTATIVE>曾小林</ns2:LEGAL_REPRESENTATIVE>
                    <ns2:TAX_REG_ADDRESS xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:OFFICE_ADDRESS xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:REGISTRY_ADDRESS>湖南省长沙市芙蓉区万家丽中路一段166号东郡华城广场1115房</ns2:REGISTRY_ADDRESS>
                    <ns2:BUSINESS_RANGE>安防设备、计算机软硬件、五金交电、办公用品、化工产品（不含危险品、监控品）、仪器仪表、电子产品、通讯器材及其配套产品、有线电视设备的研发、销售；通信工程、防雷工程、网络工程的设计；电信业务代理（不含增值电信业务）。</ns2:BUSINESS_RANGE>
                    <ns2:COMPANY_TYPE>GSLX_01</ns2:COMPANY_TYPE>
                    <ns2:BUSINESS_TIMELIMIT xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:REGISTRY_FUND>5050</ns2:REGISTRY_FUND>
                    <ns2:REGISTRY_CURRENCY>CNY</ns2:REGISTRY_CURRENCY>
                    <ns2:COUNTRY xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:STATE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:CITY xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:SOURCE_CODE>ES</ns2:SOURCE_CODE>
                    <ns2:VIRTUAL_VENDOR>N</ns2:VIRTUAL_VENDOR>
                    <ns2:VIRTUAL_TYPE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:VENDOR_TYPE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:OUTPUT_EXT xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:VENDOR_COMPANY_INFO>
                        <ns2:VENDOR_COMPANY_INFO_ITEM>
                            <ns2:VENDOR_COMPANT_ID>76772257</ns2:VENDOR_COMPANT_ID>
                            <ns2:VENDOR_ID>106393840</ns2:VENDOR_ID>
                            <ns2:ORG_NAME>OU_JS_302325_苏州分公司</ns2:ORG_NAME>
                            <ns2:OU_CODE>302325</ns2:OU_CODE>
                            <ns2:VALID_FLAG>Y</ns2:VALID_FLAG>
                            <ns2:CREATION_DATE>2019-04-12T19:55:24</ns2:CREATION_DATE>
                            <ns2:EXPIRY_DATE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                            <ns2:PURCHASE_TYPE>N</ns2:PURCHASE_TYPE>
                            <ns2:REMARK xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                            <ns2:VENDOR_ACCOUNT_INFO>
                                <ns2:VENDOR_ACCOUNT_INFO_ITEM>
                                    <ns2:VENDOR_COMPANT_ID>76772257</ns2:VENDOR_COMPANT_ID>
                                    <ns2:VENDOR_ACCOUNT_ID>14796987</ns2:VENDOR_ACCOUNT_ID>
                                    <ns2:VENDOR_ID>106393840</ns2:VENDOR_ID>
                                    <ns2:MAIN_ACCOUNT_FLAG>Y</ns2:MAIN_ACCOUNT_FLAG>
                                    <ns2:BANK_NAME>中国建设银行</ns2:BANK_NAME>
                                    <ns2:BANK_CODE>105</ns2:BANK_CODE>
                                    <ns2:BRANCH_BANK>中国建设银行股份有限公司长沙铁银支行</ns2:BRANCH_BANK>
                                    <ns2:BRANCH_CODE>105551003055</ns2:BRANCH_CODE>
                                    <ns2:CNAP_NUMBER>105551003055</ns2:CNAP_NUMBER>
                                    <ns2:ACCOUNT_NAME>长沙永讯科技有限公司</ns2:ACCOUNT_NAME>
                                    <ns2:ACCOUNT_NUMBER>43001710661052500120</ns2:ACCOUNT_NUMBER>
                                    <ns2:ACCOUNT_CURRENCY>CNY</ns2:ACCOUNT_CURRENCY>
                                    <ns2:VALID_FLAG>Y</ns2:VALID_FLAG>
                                    <ns2:OUTPUT_EXT>{"SPECIAL_ACCOUNT_FLAG":"N"}</ns2:OUTPUT_EXT>
                                </ns2:VENDOR_ACCOUNT_INFO_ITEM>
                            </ns2:VENDOR_ACCOUNT_INFO>
                            <ns2:VENDOR_CONTACTS_INFO/>
                            <ns2:VENDOR_BUSINESS_INFO/>
                            <ns2:OUTPUT_EXT xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                        </ns2:VENDOR_COMPANY_INFO_ITEM>
                    </ns2:VENDOR_COMPANY_INFO>
                    <ns2:VENDOR_ATTA_INFO/>
                </ns2:OUTPUTCOLLECTION_ITEM>
                <ns2:OUTPUTCOLLECTION_ITEM>
                    <ns2:VENDOR_ID>106393840</ns2:VENDOR_ID>
                    <ns2:VENDOR_NAME>长沙永讯科技有限公司2</ns2:VENDOR_NAME>
                    <ns2:MD_CODE>MDM_106393840</ns2:MD_CODE>
                    <ns2:CERTIFICATE_FLAG>Y</ns2:CERTIFICATE_FLAG>
                    <ns2:ORGCERT_NUMBER>77676520-3</ns2:ORGCERT_NUMBER>
                    <ns2:IDENTIFY_CODE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:SOC_IDEN>914301027767652039</ns2:SOC_IDEN>
                    <ns2:CREATION_DATE>2018-09-06T17:24:38</ns2:CREATION_DATE>
                    <ns2:VAT_FLAG>YBNSR</ns2:VAT_FLAG>
                    <ns2:TAX_REGISTRATION_NUMBER xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:STOCK_FLAG>N</ns2:STOCK_FLAG>
                    <ns2:INNER_FLAG>QW</ns2:INNER_FLAG>
                    <ns2:INNER_CODE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:TRADING_PARTY>N</ns2:TRADING_PARTY>
                    <ns2:CUSTOMER_NUMBER xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:CORPORATION_TYPE>QYFRLX1</ns2:CORPORATION_TYPE>
                    <ns2:LAST_UPDATE_DATE>2021-01-21T15:05:55</ns2:LAST_UPDATE_DATE>
                    <ns2:VENDOR_STATUS>Y</ns2:VENDOR_STATUS>
                    <ns2:VENDOR_OLD_NAME xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:LEGAL_REPRESENTATIVE>曾小林</ns2:LEGAL_REPRESENTATIVE>
                    <ns2:TAX_REG_ADDRESS xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:OFFICE_ADDRESS xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:REGISTRY_ADDRESS>湖南省长沙市芙蓉区万家丽中路一段166号东郡华城广场1115房</ns2:REGISTRY_ADDRESS>
                    <ns2:BUSINESS_RANGE>安防设备、计算机软硬件、五金交电、办公用品、化工产品（不含危险品、监控品）、仪器仪表、电子产品、通讯器材及其配套产品、有线电视设备的研发、销售；通信工程、防雷工程、网络工程的设计；电信业务代理（不含增值电信业务）。</ns2:BUSINESS_RANGE>
                    <ns2:COMPANY_TYPE>GSLX_01</ns2:COMPANY_TYPE>
                    <ns2:BUSINESS_TIMELIMIT xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:REGISTRY_FUND>5050</ns2:REGISTRY_FUND>
                    <ns2:REGISTRY_CURRENCY>CNY</ns2:REGISTRY_CURRENCY>
                    <ns2:COUNTRY xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:STATE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:CITY xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:SOURCE_CODE>ES</ns2:SOURCE_CODE>
                    <ns2:VIRTUAL_VENDOR>N</ns2:VIRTUAL_VENDOR>
                    <ns2:VIRTUAL_TYPE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:VENDOR_TYPE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:OUTPUT_EXT xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                    <ns2:VENDOR_COMPANY_INFO>
                        <ns2:VENDOR_COMPANY_INFO_ITEM>
                            <ns2:VENDOR_COMPANT_ID>62337129</ns2:VENDOR_COMPANT_ID>
                            <ns2:VENDOR_ID>106393840</ns2:VENDOR_ID>
                            <ns2:ORG_NAME>OU_CMTT_311010_中移铁通本部</ns2:ORG_NAME>
                            <ns2:OU_CODE>311010</ns2:OU_CODE>
                            <ns2:VALID_FLAG>Y</ns2:VALID_FLAG>
                            <ns2:CREATION_DATE>2018-10-31T09:30:46</ns2:CREATION_DATE>
                            <ns2:EXPIRY_DATE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                            <ns2:PURCHASE_TYPE>Y</ns2:PURCHASE_TYPE>
                            <ns2:REMARK xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                            <ns2:VENDOR_ACCOUNT_INFO>
                                <ns2:VENDOR_ACCOUNT_INFO_ITEM>
                                    <ns2:VENDOR_COMPANT_ID>62337129</ns2:VENDOR_COMPANT_ID>
                                    <ns2:VENDOR_ACCOUNT_ID>7447487</ns2:VENDOR_ACCOUNT_ID>
                                    <ns2:VENDOR_ID>106393840</ns2:VENDOR_ID>
                                    <ns2:MAIN_ACCOUNT_FLAG>Y</ns2:MAIN_ACCOUNT_FLAG>
                                    <ns2:BANK_NAME>中国建设银行</ns2:BANK_NAME>
                                    <ns2:BANK_CODE>105</ns2:BANK_CODE>
                                    <ns2:BRANCH_BANK>中国建设银行股份有限公司长沙铁银支行</ns2:BRANCH_BANK>
                                    <ns2:BRANCH_CODE>105551003055</ns2:BRANCH_CODE>
                                    <ns2:CNAP_NUMBER>105551003055</ns2:CNAP_NUMBER>
                                    <ns2:ACCOUNT_NAME>长沙永讯科技有限公司1</ns2:ACCOUNT_NAME>
                                    <ns2:ACCOUNT_NUMBER>43001710661052500120</ns2:ACCOUNT_NUMBER>
                                    <ns2:ACCOUNT_CURRENCY>CNY</ns2:ACCOUNT_CURRENCY>
                                    <ns2:VALID_FLAG>Y</ns2:VALID_FLAG>
                                    <ns2:OUTPUT_EXT>{"SPECIAL_ACCOUNT_FLAG":"N"}</ns2:OUTPUT_EXT>
                                </ns2:VENDOR_ACCOUNT_INFO_ITEM>
                            </ns2:VENDOR_ACCOUNT_INFO>
                            <ns2:VENDOR_CONTACTS_INFO>
                                <ns2:VENDOR_CONTACTS_INFO_ITEM>
                                    <ns2:VENDOR_COMPANT_ID>62337129</ns2:VENDOR_COMPANT_ID>
                                    <ns2:VENDOR_ID>106393840</ns2:VENDOR_ID>
                                    <ns2:VENDOR_CONTACTS_ID>1814217</ns2:VENDOR_CONTACTS_ID>
                                    <ns2:CONTACT_NAME>雷成华</ns2:CONTACT_NAME>
                                    <ns2:CONTACT_CELLPHONE>13974927627</ns2:CONTACT_CELLPHONE>
                                    <ns2:CONTACT_PHONE xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                                    <ns2:CONTACT_FAX xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                                    <ns2:CONTACT_MAIL>13974927627@139.com</ns2:CONTACT_MAIL>
                                    <ns2:OUTPUT_EXT xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                                </ns2:VENDOR_CONTACTS_INFO_ITEM>
                            </ns2:VENDOR_CONTACTS_INFO>
                            <ns2:VENDOR_BUSINESS_INFO/>
                            <ns2:OUTPUT_EXT xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
                        </ns2:VENDOR_COMPANY_INFO_ITEM>
                    </ns2:VENDOR_COMPANY_INFO>
                    <ns2:VENDOR_ATTA_INFO/>
                </ns2:OUTPUTCOLLECTION_ITEM>
            </ns2:OUTPUTCOLLECTION>
        </ns2:OutputParameters>
    </soap:Body>
</soapenv:Envelope>
     * </p>
     * 
     * @throws IOException
     */



    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(Xml2JsonDemo.class);
    
    
    
    public static void main(String[] args) throws Exception {
        String fillPath = "D:\\testDir\\vendor_response.txt";
        String content = FileUtils.readFileToString(new File(fillPath));
        // System.out.println(content);
        Document document = DocumentHelper.parseText(content);
        Element root = document.getRootElement();
        Element bodyElt = root.element("Body");
        Element parametersElt = bodyElt.element("OutputParameters");
        JSONObject json = new JSONObject();
        json = xml2Json(parametersElt, json);
        

        String path = "OUTPUTCOLLECTION.OUTPUTCOLLECTION_ITEM[30].VENDOR_NAME";
        String[] paths = path.split("\\.");
        Object obj = getValue(paths, 0, json);
        System.out.println(obj);
    }
    

    /**
     * 根据Json路径从JSONObject中获取对应的值
     * 
     * @param json
     *            json对象
     * @param path
     *            json路径
     * @return json路径对应的值
     * @author Monk
     * @date 2021年1月25日 下午6:06:40
     */
    public static Object getValue(JSONObject json, String path) {
        String[] paths = path.split("\\.");
        return getValue(paths, 0, json);
    }

    /**
     * 根据Json路径从JSONObject中获取对应的值
     * 
     * @param paths
     *            路径切割后的分组
     * @param index
     *            下标
     * @param json
     *            json对象
     * 
     * @return json路径对应的值
     * @author Monk
     * @date 2021年1月25日 下午6:07:33
     */
    @SuppressWarnings("unused")
    private static Object getValue(String[] paths, int index, JSONObject json) {
        for (int i = index; i < paths.length; i++) {
            String path = paths[i];
            int stardIdx = path.indexOf("[");
            int endIdx = path.indexOf("]");
            int idx = 0;
            if (stardIdx > 0 && endIdx > 0) {
                try {
                    idx = Integer.valueOf(path.substring(stardIdx + 1, endIdx));
                } catch (NumberFormatException e) {
                    logger.warn("The default value[0] will be userd.");
                    idx = 0;
                }
                path = path.substring(0, stardIdx);
            }
            Object obj = json.get(path);
            if (null == obj) {
                return null;
            }
            if (obj instanceof JSONObject) {
                return getValue(paths, i + 1, (JSONObject) obj);
            } else if (obj instanceof JSONArray) {
                JSONArray arr = (JSONArray) obj;
                if (idx > arr.size()) {
                    idx = arr.size() - 1;
                    logger.warn("If the array length is exceeded, the maximum array value[{}] will be used.", idx);
                }
                JSONObject jObj = arr.getJSONObject(idx);
                return getValue(paths, i + 1, jObj);
            } else {
                return obj;
            }
        }
        return null;
    }

    /**
     * dom4j Xml转换成JSONObject
     * 
     * @param element
     *            xml节点
     * @param json
     *            JSONObject对象
     * @return JSONObject对象
     * @author Monk
     * @date 2021年1月25日 下午6:04:32
     */
    @SuppressWarnings("unchecked")
    public static JSONObject xml2Json(Element element, JSONObject json) {
        for (Object o : element.attributes()) {
            Attribute attr = (Attribute) o;
            if (!isEmpty(attr.getValue())) {
                json.put("@" + attr.getName(), attr.getValue());
            }
        }
        List<Element> chdEl = element.elements();
        if (chdEl.isEmpty() && !isEmpty(element.getText())) {
            // 如果没有子元素,只有一个值
            json.put(element.getName(), element.getText());
        }

        for (Element e : chdEl) {
            // 有子元素
            if (!e.elements().isEmpty()) {
                // 子元素也有子元素
                JSONObject chdjson = new JSONObject();
                xml2Json(e, chdjson);
                Object o = json.get(e.getName());
                if (o != null) {
                    JSONArray jsona = null;
                    if (o instanceof JSONObject) {
                        // 如果此元素已存在,则转为jsonArray
                        JSONObject jsono = (JSONObject) o;
                        json.remove(e.getName());
                        jsona = new JSONArray();
                        jsona.add(jsono);
                        jsona.add(chdjson);
                    }
                    if (o instanceof JSONArray) {
                        jsona = (JSONArray) o;
                        jsona.add(chdjson);
                    }
                    json.put(e.getName(), jsona);
                } else {
                    if (!chdjson.isEmpty()) {
                        json.put(e.getName(), chdjson);
                    }
                }

            } else {
                // 子元素没有子元素
                for (Object o : element.attributes()) {
                    Attribute attr = (Attribute) o;
                    if (!isEmpty(attr.getValue())) {
                        json.put("@" + attr.getName(), attr.getValue());
                    }
                }
                if (!e.getText().isEmpty()) {
                    json.put(e.getName(), e.getText());
                }
            }
        }
        return json;
    }

    /**
     * 判断值是否为空(null, "", "null"均为空)
     * 
     * @param str
     *            字符串
     * @return true:空 false:非空
     * 
     * @author Monk
     * @date 2021年1月25日 下午6:05:18
     */
    private static boolean isEmpty(String str) {
        if (str == null || str.trim().isEmpty() || "null".equals(str)) {
            return true;
        }
        return false;
    }



}


package com.monk.commondas;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>InputParameters complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="InputParameters">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SERVICE_NAME_EN" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MAJOR_VERSION" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DATA_CONFIG" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CONFIG_VERSION" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="INPUT_JSON" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PAGE_SIZE" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="CURRENT_PAGE" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="TOTAL_RECORD" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InputParameters", propOrder = {
    "servicenameen",
    "majorversion",
    "dataconfig",
    "configversion",
    "inputjson",
    "pagesize",
    "currentpage",
    "totalrecord"
})
public class InputParameters {

    @XmlElement(name = "SERVICE_NAME_EN", required = true, nillable = true)
    protected String servicenameen;
    @XmlElement(name = "MAJOR_VERSION", required = true, nillable = true)
    protected String majorversion;
    @XmlElement(name = "DATA_CONFIG", required = true, nillable = true)
    protected String dataconfig;
    @XmlElement(name = "CONFIG_VERSION", required = true, nillable = true)
    protected String configversion;
    @XmlElement(name = "INPUT_JSON", required = true, nillable = true)
    protected String inputjson;
    @XmlElement(name = "PAGE_SIZE", required = true, nillable = true)
    protected BigDecimal pagesize;
    @XmlElement(name = "CURRENT_PAGE", required = true, nillable = true)
    protected BigDecimal currentpage;
    @XmlElement(name = "TOTAL_RECORD", required = true, nillable = true)
    protected BigDecimal totalrecord;

    /**
     * 获取servicenameen属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSERVICENAMEEN() {
        return servicenameen;
    }

    /**
     * 设置servicenameen属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSERVICENAMEEN(String value) {
        this.servicenameen = value;
    }

    /**
     * 获取majorversion属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAJORVERSION() {
        return majorversion;
    }

    /**
     * 设置majorversion属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAJORVERSION(String value) {
        this.majorversion = value;
    }

    /**
     * 获取dataconfig属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATACONFIG() {
        return dataconfig;
    }

    /**
     * 设置dataconfig属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATACONFIG(String value) {
        this.dataconfig = value;
    }

    /**
     * 获取configversion属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCONFIGVERSION() {
        return configversion;
    }

    /**
     * 设置configversion属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCONFIGVERSION(String value) {
        this.configversion = value;
    }

    /**
     * 获取inputjson属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINPUTJSON() {
        return inputjson;
    }

    /**
     * 设置inputjson属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINPUTJSON(String value) {
        this.inputjson = value;
    }

    /**
     * 获取pagesize属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPAGESIZE() {
        return pagesize;
    }

    /**
     * 设置pagesize属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPAGESIZE(BigDecimal value) {
        this.pagesize = value;
    }

    /**
     * 获取currentpage属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCURRENTPAGE() {
        return currentpage;
    }

    /**
     * 设置currentpage属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCURRENTPAGE(BigDecimal value) {
        this.currentpage = value;
    }

    /**
     * 获取totalrecord属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTOTALRECORD() {
        return totalrecord;
    }

    /**
     * 设置totalrecord属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTOTALRECORD(BigDecimal value) {
        this.totalrecord = value;
    }

}


package com.monk.commondas;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>OutputParameters complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="OutputParameters">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ESB_FLAG" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ESB_RETURN_CODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ESB_RETURN_MESSAGE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BIZ_SERVICE_FLAG" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BIZ_RETURN_CODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BIZ_RETURN_MESSAGE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="INSTANCE_ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TOTAL_RECORD" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="OUTPUT_JSON" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutputParameters", propOrder = {
    "esbflag",
    "esbreturncode",
    "esbreturnmessage",
    "bizserviceflag",
    "bizreturncode",
    "bizreturnmessage",
    "instanceid",
    "totalrecord",
    "outputjson"
})
public class OutputParameters {

    @XmlElement(name = "ESB_FLAG", required = true, nillable = true)
    protected String esbflag;
    @XmlElement(name = "ESB_RETURN_CODE", required = true, nillable = true)
    protected String esbreturncode;
    @XmlElement(name = "ESB_RETURN_MESSAGE", required = true, nillable = true)
    protected String esbreturnmessage;
    @XmlElement(name = "BIZ_SERVICE_FLAG", required = true, nillable = true)
    protected String bizserviceflag;
    @XmlElement(name = "BIZ_RETURN_CODE", required = true, nillable = true)
    protected String bizreturncode;
    @XmlElement(name = "BIZ_RETURN_MESSAGE", required = true, nillable = true)
    protected String bizreturnmessage;
    @XmlElement(name = "INSTANCE_ID", required = true, nillable = true)
    protected String instanceid;
    @XmlElement(name = "TOTAL_RECORD", required = true, nillable = true)
    protected BigDecimal totalrecord;
    @XmlElement(name = "OUTPUT_JSON", required = true, nillable = true)
    protected String outputjson;

    /**
     * 获取esbflag属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getESBFLAG() {
        return esbflag;
    }

    /**
     * 设置esbflag属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setESBFLAG(String value) {
        this.esbflag = value;
    }

    /**
     * 获取esbreturncode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getESBRETURNCODE() {
        return esbreturncode;
    }

    /**
     * 设置esbreturncode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setESBRETURNCODE(String value) {
        this.esbreturncode = value;
    }

    /**
     * 获取esbreturnmessage属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getESBRETURNMESSAGE() {
        return esbreturnmessage;
    }

    /**
     * 设置esbreturnmessage属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setESBRETURNMESSAGE(String value) {
        this.esbreturnmessage = value;
    }

    /**
     * 获取bizserviceflag属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBIZSERVICEFLAG() {
        return bizserviceflag;
    }

    /**
     * 设置bizserviceflag属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBIZSERVICEFLAG(String value) {
        this.bizserviceflag = value;
    }

    /**
     * 获取bizreturncode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBIZRETURNCODE() {
        return bizreturncode;
    }

    /**
     * 设置bizreturncode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBIZRETURNCODE(String value) {
        this.bizreturncode = value;
    }

    /**
     * 获取bizreturnmessage属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBIZRETURNMESSAGE() {
        return bizreturnmessage;
    }

    /**
     * 设置bizreturnmessage属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBIZRETURNMESSAGE(String value) {
        this.bizreturnmessage = value;
    }

    /**
     * 获取instanceid属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSTANCEID() {
        return instanceid;
    }

    /**
     * 设置instanceid属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSTANCEID(String value) {
        this.instanceid = value;
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

    /**
     * 获取outputjson属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOUTPUTJSON() {
        return outputjson;
    }

    /**
     * 设置outputjson属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOUTPUTJSON(String value) {
        this.outputjson = value;
    }

}

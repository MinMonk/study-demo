package com.monk.common.jaxb.bean;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Key {

    @XmlElement(name = "PARENT_FIELD", required = true, nillable = false)
    private String parentField;

    @XmlElement(name = "CHILD_FIELD", required = true, nillable = false)
    private String childField;

    public String getParentField() {
        return parentField;
    }

    public void setParentField(String parentField) {
        this.parentField = parentField;
    }

    public String getChildField() {
        return childField;
    }

    public void setChildField(String childField) {
        this.childField = childField;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}

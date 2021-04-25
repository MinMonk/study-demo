package com.monk.commondas.common;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class OutputJson implements Serializable {

    private static final long serialVersionUID = -515319389066074160L;

    @JSONField(name = "E_TYPE")
    private String eType;

    @JSONField(name = "E_MESSAGE")
    private String eMessage;

    public String geteType() {
        return eType;
    }

    public void seteType(String eType) {
        this.eType = eType;
    }

    public String geteMessage() {
        return eMessage;
    }

    public void seteMessage(String eMessage) {
        this.eMessage = eMessage;
    }

}

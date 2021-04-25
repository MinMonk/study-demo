package com.monk.framework;

public enum ScopeType {

    SINGLETON("singleton"), PROTOTYPE("prototype");

    private String value;

    private ScopeType(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}

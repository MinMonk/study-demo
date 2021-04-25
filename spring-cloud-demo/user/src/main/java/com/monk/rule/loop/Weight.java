package com.monk.rule.loop;

public class Weight {

    private Integer currWeight;

    private Integer weight;

    private String ip;

    public Integer getCurrWeight() {
        return currWeight;
    }

    public void setCurrWeight(Integer currWeight) {
        this.currWeight = currWeight;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Weight(Integer currWeight, Integer weight, String ip) {
        this.currWeight = currWeight;
        this.weight = weight;
        this.ip = ip;
    }
}

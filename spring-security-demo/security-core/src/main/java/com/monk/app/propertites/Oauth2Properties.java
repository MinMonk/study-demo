package com.monk.app.propertites;

/**
 * @ClassName Oauth2Properties
 * @Description: TODO
 * @Author Monk
 * @Date 2020/7/5
 * @Version V1.0
 **/
public class Oauth2Properties {

    private String jwtSignKey = "monk";

    private Oauth2ClientProerties[] clients = {};

    public Oauth2ClientProerties[] getClients() {
        return clients;
    }

    public void setClients(Oauth2ClientProerties[] clients) {
        this.clients = clients;
    }

    public String getJwtSignKey() {
        return jwtSignKey;
    }

    public void setJwtSignKey(String jwtSignKey) {
        this.jwtSignKey = jwtSignKey;
    }
}

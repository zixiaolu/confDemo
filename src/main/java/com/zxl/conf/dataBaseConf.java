package com.zxl.conf;

import java.util.Map;

public class dataBaseConf {
    Map<String,Serv> database;
    String clientIp;

    public Map<String, Serv> getDatabase() {
        return database;
    }

    public void setDatabase(Map<String, Serv> database) {
        this.database = database;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
}

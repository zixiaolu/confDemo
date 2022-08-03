package com.zxl.conf;

import java.util.Map;

public class TomlConf {
    Map<String,Serv> database;
    Map<String,Serv> redis;
    String clientIp;

    public Map<String, Serv> getDatabase() {
        return database;
    }

    public void setDatabase(Map<String, Serv> database) {
        this.database = database;
    }

    public Map<String, Serv> getRedis() {
        return redis;
    }

    public void setRedis(Map<String, Serv> redis) {
        this.redis = redis;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
}

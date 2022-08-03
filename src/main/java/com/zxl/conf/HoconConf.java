package com.zxl.conf;

import java.util.Map;

public class HoconConf {
    Map<String,Object> database;
    String clientIp;
    Map<String,Object> redis;

    public Map<String, Object> getDatabase() {
        return database;
    }

    public void setDatabase(Map<String, Object> database) {
        this.database = database;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Map<String, Object> getRedis() {
        return redis;
    }

    public void setRedis(Map<String, Object> redis) {
        this.redis = redis;
    }
}

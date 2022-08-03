package com.zxl.conf;

import java.util.Map;

public class dataBaseConf {
    Map<String,dataSources> database;

    public Map<String, dataSources> getDatabase() {
        return database;
    }

    public void setDatabase(Map<String, dataSources> database) {
        this.database = database;
    }
}

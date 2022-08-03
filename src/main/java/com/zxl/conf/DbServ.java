package com.zxl.conf;

import java.util.Map;

public class DbServ {
    Map<String,Serv> database;

    public Map<String, Serv> getDatabase() {
        return database;
    }

    public void setDatabase(Map<String, Serv> database) {
        this.database = database;
    }
}

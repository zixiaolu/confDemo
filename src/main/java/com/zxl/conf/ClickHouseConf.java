package com.zxl.conf;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import com.zxl.conftest.MainApplication;
import java.io.IOException;
import java.io.InputStream;


public class ClickHouseConf {
    String clickHouseUrl;
    String username;
    String password;

    private static ClickHouseConf instance=null;

    private ClickHouseConf() {
        clickHouseUrl = "jdbc:clickhouse://192.168.6.130:8123/zombie_log?user=zxl&password=qwerty&decompress=0";
    }

    public static ClickHouseConf getInstance(){
        if(instance==null)
        {
            instance = new ClickHouseConf();
        }
        return instance;
    }

    public String getClickHouseUrl() {
        return clickHouseUrl;
    }

    public void setClickHouseUrl(String clickHouseUrl) {
        this.clickHouseUrl = clickHouseUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

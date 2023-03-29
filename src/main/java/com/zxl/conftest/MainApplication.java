package com.zxl.conftest;
import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;
import com.zxl.clickHouse.ClickHouseLogic;
import com.zxl.conf.HoconConf;
import com.zxl.conf.dataBaseConf;
import com.zxl.conf.TomlConf;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;


public class  MainApplication {
    public static void main(String[] args) throws SQLException, InterruptedException {
        ClickHouseLogic clickHouseLogic = new ClickHouseLogic();
        clickHouseLogic.testBatchQuery();
    }

    public static void parseYaml(){
        Yaml yaml =new Yaml(new Constructor(dataBaseConf.class));
        InputStream content = MainApplication.class.getClassLoader().getResourceAsStream("database.yml");
        Iterable<Object> its = yaml.loadAll(content);
        for(Object x : its){
            dataBaseConf dbMp = (dataBaseConf)x;
            System.out.println(dbMp.getDatabase().get("2001"));
            System.out.println(dbMp.getDatabase().get("2002"));
            System.out.println(dbMp.getDatabase().get("2003"));
        }
    }

    public static void paresToml() throws IOException {
        InputStream content = MainApplication.class.getClassLoader().getResourceAsStream("database3.toml");
        TomlMapper tomlMapper = new TomlMapper();
        TomlConf result = tomlMapper.readValue(content, TomlConf.class);
        System.out.println(result.getClientIp());
        System.out.println(result.getDatabase().get("2001"));
        System.out.println(result.getDatabase().get("2002"));
        System.out.println(result.getDatabase().get("2003"));
        System.out.println(result.getRedis().get("2001"));
    }

    public static void paresYamlByJackSon() throws IOException {
        InputStream content = MainApplication.class.getClassLoader().getResourceAsStream("database.yml");
        YAMLMapper mapper = new YAMLMapper();
        TomlConf result = mapper.readValue(content, TomlConf.class);
        System.out.println(result.getDatabase().get("2001"));
        System.out.println(result.getDatabase().get("2002"));
        System.out.println(result.getDatabase().get("2003"));
    }

    public static void paresHocon(){
        Config conf = ConfigFactory.load("database4.conf");
        HoconConf hoconConf = ConfigBeanFactory.create(conf, HoconConf.class);
        System.out.println(hoconConf.getClientIp());
        System.out.println(hoconConf.getDatabase().get("2001"));
        System.out.println(hoconConf.getDatabase().get("2002"));
    }

    public static void MutiTest()
    {
        ThreadPool pool = new ThreadPool(10);
        for(int i=1;i<=3;i++)
        {
            if(i==1)
            {
                pool.execute(()->{
                    try {
                        new ClickHouseLogic().testBatchInsert();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
            else
            {
                pool.execute(()->{
                    try {
                        new ClickHouseLogic().testBatchQuery();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

}
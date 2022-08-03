package com.zxl.conftest;
import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;
import com.zxl.conf.dataBaseConf;
import com.zxl.conf.DbServ;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;


public class  MainApplication {
    public static void main(String[] args) throws IOException {
        parseYaml();
        paresToml();
        paresHocon();
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
        DbServ result = tomlMapper.readValue(content, DbServ.class);
        System.out.println(result.getDatabase().get("2001"));
        System.out.println(result.getDatabase().get("2002"));
        System.out.println(result.getDatabase().get("2003"));
    }

    public static void paresYamlByJackSon() throws IOException {
        InputStream content = MainApplication.class.getClassLoader().getResourceAsStream("database.yml");
        YAMLMapper mapper = new YAMLMapper();
        DbServ result = mapper.readValue(content, DbServ.class);
        System.out.println(result.getDatabase().get("2001"));
        System.out.println(result.getDatabase().get("2002"));
        System.out.println(result.getDatabase().get("2003"));
    }

    public static void paresHocon(){
        Config conf = ConfigFactory.load("database4.conf");
        DbServ dbServs = ConfigBeanFactory.create(conf, DbServ.class);
        System.out.println(dbServs.getDatabase().get("2001"));
        System.out.println(dbServs.getDatabase().get("2002"));
        System.out.println(dbServs.getDatabase().get("2003"));
    }

}
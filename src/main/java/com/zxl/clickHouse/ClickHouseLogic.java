package com.zxl.clickHouse;

import com.clickhouse.client.*;
import com.clickhouse.client.config.ClickHouseClientOption;
import com.clickhouse.data.ClickHouseFormat;
import com.clickhouse.data.ClickHouseRecord;
import com.clickhouse.jdbc.ClickHouseDataSource;
import com.clickhouse.jdbc.ClickHouseResultSet;
import com.clickhouse.jdbc.ClickHouseStatement;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zxl.conf.ClickHouseConf;


import java.lang.reflect.Field;
import java.sql.*;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;

import static com.clickhouse.client.config.ClickHouseClientOption.COMPRESS;

public class ClickHouseLogic {

    public static HikariDataSource ds = null;

/*    private static Connection getConnection(String url) throws SQLException {
        return getConnection(url, new Properties());
    }*/

    private static Connection getConnection(String url, Properties properties) throws SQLException {
        final Connection conn;
        // Driver driver = new ClickHouseDriver();
        // conn = driver.connect(url, properties);

        // ClickHouseDataSource dataSource = new ClickHouseDataSource(url, properties);
        // conn = dataSource.getConnection();
        conn = DriverManager.getConnection(url, properties);
        System.out.println("Connected to: " + conn.getMetaData().getURL());
        return conn;
    }

    static Connection getConnection() throws SQLException {
        // connection pooling won't help much in terms of performance,
        // because the underlying implementation has its own pool.
        // for example: HttpURLConnection has a pool for sockets
        if(ds==null)
        {
            HikariConfig poolConfig = new HikariConfig();
            poolConfig.setConnectionTimeout(5000L);
            poolConfig.setMaximumPoolSize(20);
            poolConfig.setMaxLifetime(300000L);
            poolConfig.setDataSource(new ClickHouseDataSource(ClickHouseConf.getInstance().getClickHouseUrl()));
            ds = new HikariDataSource(poolConfig);
        }
        return ds.getConnection();
    }

    public void testQuery() throws SQLException, InterruptedException {
        Properties properties = new Properties();
        Connection conn = ClickHouseLogic.getConnection();
        PreparedStatement stmt = conn.prepareStatement("select * from schedule_log where id = 93371");
        int upCount = 0;
        int downCount = 0;
        long totalTime = 0;
        for(int i=1;i<=10;i++)
        {
            long beforeTime = System.nanoTime();
            stmt.execute();
            long afterTime = System.nanoTime();
            long det = afterTime-beforeTime;
            totalTime+=det;
            System.out.println("Query "+i+" time delta:"+det);
            downCount++;
            Thread.sleep(1000);

        }
        System.out.println(Thread.currentThread().getName()+"->"+(totalTime/downCount)+" op:select");
    }


    public void testBatchQuery() throws SQLException{
        Properties properties = new Properties();
        Connection conn = getConnection(ClickHouseConf.getInstance().getClickHouseUrl(),properties);
        PreparedStatement stmt = conn.prepareStatement("select * from schedule_log where id>=? and id <=?");
        double x = Math.random()*10000f;
        double y = Math.random()*10000f;
        int id1 = (int) Math.round(x);
        int id2 = (int) Math.round(y);
        id1-=10000;
        id2+=10000;
        int up = Math.max(id1,id2);
        int down = Math.min(id1,id2);
        stmt.setInt(1,down);
        stmt.setInt(2,up);
        System.out.println(Thread.currentThread().getName()+" MutiQuery start"+System.currentTimeMillis());
        long beforeTime = System.nanoTime();
        stmt.execute();
        long afterTime = System.nanoTime();
        System.out.println(Thread.currentThread().getName()+" MutiQuery end"+System.currentTimeMillis());
        long det = afterTime-beforeTime;
        ResultSet set = stmt.getResultSet();
        ClickHouseResultSet resultSet = (ClickHouseResultSet) set;


/*        Field responseFile = resultSet.getClass().getField("response");
        responseFile.setAccessible(true);
        ClickHouseResponse clickHouseResponse = (ClickHouseResponse) responseFile.get(resultSet);
        clickHouseResponse.get*/
    }


    public void testBatchInsert() throws SQLException {
        Properties properties = new Properties();
        Connection conn = ClickHouseLogic.getConnection();
        PreparedStatement stmt = conn.prepareStatement("数据");
        int tmp = 1000000;
        long t1 = 1679500800000l;
        long t2 = 1679587200000l;
        long t3 = 1679673600000l;
        int k = 1;
        for(int i=1;i<=500000;i++)
        {
            if(k>=3) k = 1;
            stmt.setLong(1,System.currentTimeMillis());
            if(k==1)
            {
                stmt.setLong(2,t1);
            }
            else if(k==2)
            {
                stmt.setLong(2,t2);
            }
            else
            {
                stmt.setLong(2,t3);
            }
            k++;
            stmt.addBatch();
        }
        System.out.println(Thread.currentThread().getName()+" BatchInsert start:"+System.currentTimeMillis());
        long beforeTime = System.nanoTime();
        stmt.executeBatch();
        long afterTime = System.nanoTime();
        System.out.println(Thread.currentThread().getName()+" BatchInsert end:"+System.currentTimeMillis());
        long det = afterTime-beforeTime;
        System.out.println(Thread.currentThread().getName()+"->"+det+" op:insert");
    }

    public void testDelete() throws SQLException {
        Connection conn = ClickHouseLogic.getConnection();
        PreparedStatement stmt = conn.prepareStatement("delete from 表名 where (log_time/1000) = 1679500800");
        int id = 1000;
        System.out.println(Thread.currentThread().getName()+"-->Delete Data start:"+System.currentTimeMillis());
        long beforeTime = System.nanoTime();
        stmt.executeBatch();
        long afterTime = System.nanoTime();
        System.out.println(Thread.currentThread().getName()+"-->Delete Data end:"+System.currentTimeMillis());
        long det = afterTime-beforeTime;
        System.out.println(Thread.currentThread().getName()+"->op:delete->"+det);
    }

    public void testSingleInsert() throws SQLException {
        Connection conn = ClickHouseLogic.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO zombie_log.schedule_log(id,log_type,log_time,region_id,server_id,account_id,account_name,char_id,char_name,`level`,vip_level,reason,param,time_zone,base_sdk_enum,base_ad_spread_channel,base_createtime,base_wave_id,base_fight,base_create_days,createTime) VALUES " +
                " (?, 908,?, 1, 1102, -1, '-1', -1, '-1', -1, -1, 1, '在线人数定时检测器:0', '', -1, -1, '-1', -1, -1, -1, 1679414418813);");
        for(int i=1;i<=100;i++)
        {
            long time = System.currentTimeMillis();
            stmt.setLong(1,time);
            stmt.setLong(2,time);
            System.out.println(Thread.currentThread().getName()+"-->Insert Data--->"+time);
            stmt.execute();
        }
    }

}

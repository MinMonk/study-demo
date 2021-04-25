package com.monk.commondas.common;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.lang.StringUtils;


/**
 * 数据源管理类
 * 
 * @author houyongchuan
 *
 */
public class DataSourceManager {

    public static DataSource createDataSource(String username, String password, String url, String driver)
            throws Exception {
        return createDataSource(username, password, url, driver, null, null, null, null, null, null, null);
    }

    /**
     * 创建数据源 使用DBCP创建BasicDataSource数据源,属性设置请参考DBCP属性
     * 
     * @param username
     * @param password
     * @param url
     * @param driver
     * @param maxActive
     * @param maxIdle
     * @param maxWait
     * @param removeAbandoned
     * @param removeAbandonedTimeout
     * @param testOnBorrow
     * @param logAbandoned
     * @return
     * @throws Exception
     */
    public static DataSource createDataSource(String username, String password, String url, String driver,
            String maxActive, String maxIdle, String maxWait, String removeAbandoned, String removeAbandonedTimeout,
            String testOnBorrow, String logAbandoned) throws Exception {

        Properties config = new Properties();
        config.setProperty("driverClassName", driver);
        config.setProperty("url", url);
        config.setProperty("password", password);
        config.setProperty("username", username);
        config.setProperty("maxActive", maxActive);
        config.setProperty("maxIdle", maxIdle);
        config.setProperty("maxWait", maxWait);
        config.setProperty("removeAbandoned", removeAbandoned);
        config.setProperty("removeAbandonedTimeout", removeAbandonedTimeout);
        config.setProperty("testOnBorrow", testOnBorrow);
        config.setProperty("logAbandoned", logAbandoned);

        DataSource dataSource = createDataSource(config);
        return dataSource;
    }

    /**
     * 创建数据源 使用DBCP创建BasicDataSource数据源,属性设置请参考DBCP属性
     * 
     * @param properties
     * @return
     * @throws Exception
     */
    public static DataSource createDataSource(Properties properties) throws Exception {
        if (properties != null) {
            Iterator<Entry<Object, Object>> entrys = properties.entrySet().iterator();
            List<String> removeKet = new ArrayList<String>();
            while (entrys.hasNext()) {
                Entry<Object, Object> entry = entrys.next();
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                if (StringUtils.isBlank(String.valueOf(value))) {
                    removeKet.add(key);
                    // properties.remove(key);
                }
            }

            for (String key : removeKet) {
                properties.remove(key);
            }
        }
        DataSource dataSource = BasicDataSourceFactory.createDataSource(properties);
        return dataSource;
    }

    /**
     * 关闭数据源 调用DBCP的BasicDataSource数据源的close方法 其他类型的数据源设为null
     * 
     * @param dataSource
     */
    public static void close(DataSource dataSource) {
        if (dataSource != null && dataSource instanceof BasicDataSource) {
            BasicDataSource basicDataSource = (BasicDataSource) dataSource;
            try {
                basicDataSource.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            dataSource = null;
        }
    }

}

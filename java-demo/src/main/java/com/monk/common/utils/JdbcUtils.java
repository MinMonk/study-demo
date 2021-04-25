/**
 * 
 * 文件名：JdbcUtils.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.common.utils;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.sql.BLOB;
import oracle.sql.CLOB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2018年11月27日 下午2:59:14
 */
public class JdbcUtils {

    private static final String URL="jdbc:oracle:thin:@10.204.105.127:1521:db11g02";
    private static final String USER="CSBHDDEV";
    private static final String PASSWORD="AAaa1234";
    
    private static Logger logger = LoggerFactory.getLogger(JdbcUtils.class);
    
    private static Connection conn = null;

    static {
        try {
            // 1.加载驱动程序
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // 2.获得数据库的连接
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return conn;
    }
    
    public static List<Map<String, Object>> queryByNativeSql(String sql, Object... params){
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    st.setObject(i + 1, params[i]);
                }
            }
            rs = st.executeQuery();
            final ResultSetMetaData rsm = rs.getMetaData();
            while (rs.next()) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                for (int col = 0; col < rsm.getColumnCount(); col++) {
                    // 2017/7/20 CLOB BLOB 转string
                    Object value = rs.getObject(col + 1);
                    map.put(rsm.getColumnLabel(col + 1).toUpperCase(), value);
                }
                resultList.add(map);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
    
    /**
     * 
     * 执行sql语句
     * 
     * @param sql
     *            sql语句
     * @param params
     *            参数值
     * @return sql语句是否执行成功标识 true or false
     * @throws SQLException
     * @author xiaoling
     * @date 2018年4月5日 上午11:34:43
     */
    public static boolean excuteNativeSql(String sql, Object... params) throws SQLException {
        Connection conn = getConnection();
        if (conn == null) {
            return false;
        }
        return excuteNativeSql(conn, sql, params);
    }
    
    /**
     * 
     * 执行sql语句
     * 
     * @param conn
     *            连接
     * @param sql
     *            sql语句
     * @param params
     *            参数值
     * @return sql语句是否执行成功标识 true or false
     * @throws SQLException
     * @author xiaoling
     * @date 2018年4月5日 上午11:39:33
     */
    public static boolean excuteNativeSql(Connection conn, String sql, Object... params) throws SQLException {
        if (conn == null) {
            return false;
        }
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    st.setObject(i + 1, params[i]);
                }
            }
            st.execute();
            conn.commit();
            st.close();
            return true;
        } catch (SQLException e) {
            throw e;
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * Clob转string
     *
     * @param value
     * @return
     * @throws Exception
     */
    public static String transClobToString(CLOB value) throws Exception {
        if (value != null) {
            Reader is = value.getCharacterStream();// 得到流
            BufferedReader br = new BufferedReader(is);
            String s = br.readLine();
            StringBuffer sb = new StringBuffer();
            while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
                sb.append(s);
                s = br.readLine();
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    /**
     * Blob转string
     *
     * @param value
     * @return
     * @throws Exception
     */
    public static String transBlobToString(BLOB value) throws Exception {
        if (value != null) {
            return new String(value.getBytes(1, (int) value.length()));
        } else {
            return "";
        }
    }
    
    /**
     * 
     * 关闭数据库连接
     * 
     * @param conn
     *            连接
     * @author xiaoling
     * @date 2018年4月5日 上午11:33:01
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}

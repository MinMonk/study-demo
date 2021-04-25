package com.monk.commondas.util;

import com.monk.commondas.common.DataSourceManager;
import com.monk.commondas.common.SpringContextHolder;
import oracle.sql.BLOB;
import oracle.sql.CLOB;
import org.apache.commons.dbcp.DelegatingConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.*;

public class JDBCUtil {

    private final static Logger logger = LoggerFactory.getLogger(JDBCUtil.class);

    // 数据源
    private static DataSource ds;

    private static String dsName = "java:/datasources/visesbdb";

    private static Map<String, DataSource> dataSources = new HashMap<String, DataSource>();

    /**
     * 获取数据源
     * 
     * @param jndi
     * @return
     */
    public static DataSource getDataSource(String jndi) {
        DataSource dataSource = dataSources.get(jndi);
        if (dataSource == null) {
            try {
                Context ctx = new InitialContext();
                dataSource = (DataSource) ctx.lookup(jndi);
                dataSources.put(jndi, dataSource);
            } catch (NamingException e) {
                logger.error("Can not lookup the data source which named : " + dsName, e);
            }
        }

        return dataSource;
    }

    /**
     * 根据dataSourceName 获取数据源 从缓存中获取数据源，存在则返回 不存在，根据properties创建数据源
     * 
     * @param dataSourceName
     * @param properties
     * @return
     * @throws Exception
     */
    public static DataSource getDataSource(String dataSourceName, Properties properties) throws Exception {
        DataSource dataSource = dataSources.get(dataSourceName);
        if (dataSource == null) {
            dataSource = DataSourceManager.createDataSource(properties);
            dataSources.put(dataSourceName, dataSource);
        }
        return dataSource;
    }

    /**
     * 刷新数据源
     * 
     * @param dataSourceName
     * @param properties
     * @return
     * @throws Exception
     */
    public static DataSource refreshDataSource(String dataSourceName, Properties properties) throws Exception {
        DataSource oldDataSource = dataSources.get(dataSourceName);
        DataSource dataSource = null;
        if (oldDataSource != null) {
            try {
                dataSource = DataSourceManager.createDataSource(properties);
                if (dataSource != null) {
                    DataSourceManager.close(oldDataSource);
                    dataSources.put(dataSourceName, dataSource);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return dataSource;
    }

    public static void removeDataSource(String dataSourceName) {
        DataSource oldDataSource = dataSources.get(dataSourceName);
        if (oldDataSource != null) {
            DataSourceManager.close(oldDataSource);
            dataSources.remove(dataSourceName);
        }
    }

    public static boolean testDataSource(String dataSourceName, Properties properties) throws Exception {
        DataSource dataSource = null;
        Connection conn = null;
        try {
            dataSource = DataSourceManager.createDataSource(properties);
            conn = dataSource.getConnection();
            if (conn != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (conn != null) {
                closeConnection(conn);
            }
            if (dataSource != null) {
                DataSourceManager.close(dataSource);
            }
        }
    }

    /**
     * 根据dataSourceName 获取数据链接 从缓存中获取数据源，然后再获取数据库链接
     * 缓存中不存在数据源，根据properties创建数据源，再获取数据库链接
     * 
     * @param dataSourceName
     * @param properties
     * @return
     * @throws Exception
     */
    public static Connection getConnection(String dataSourceName, Properties properties) throws Exception {
        DataSource dataSource = getDataSource(dataSourceName, properties);
        Connection conn = null;
        if (dataSource != null) {
            try {
                conn = dataSource.getConnection();
            } catch (SQLException e) {
                logger.error("Get Connection failed ...", e);
            }
        }
        return conn;
    }

    /**
     * 获取数据库连接
     * 
     * @param jndi
     * @return
     */
    public static Connection getConnection(String jndi) {
        DataSource datasource = getDataSource(jndi);
        Connection conn = null;
        if (datasource != null) {
            try {
                conn = datasource.getConnection();
            } catch (SQLException e) {
                logger.error("Get Connection failed ...", e);
            }
        }
        return conn;
    }

    /**
     * 获取数据库连接
     * 
     * @return 数据库连接/null
     */
    public static Connection getConnection() {
        if (ds == null) {
            ds = (DataSource) SpringContextHolder.getBean("dataSource");
        }

        Connection conn = null;
        if (ds != null) {
            try {
                conn = ds.getConnection();
            } catch (SQLException e) {
                logger.error("Get Connection failed ...", e);
            }
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     * 
     * @param conn
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

    /**
     * 
     * 通过查询sql获取结果集中第一行，第一列的值，默认jndi "java:/datasources/visesbdb"
     * 
     * @param 查询sql
     * @return
     * @throws SQLException
     */
    public static Object QueryUniqueResult(String query) throws SQLException {
        Connection conn = getConnection();
        return QueryUniqueResult(conn, query);
    }

    /**
     * 通过查询sql获取结果集中第一行，第一列的值
     * 
     * @param conn
     * @param query
     * @return
     * @throws SQLException
     */
    public static Object QueryUniqueResult(Connection conn, String query) throws SQLException {
        Object result = null;
        if (conn != null) {
            try {
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    result = rs.getObject(1);
                    if (result instanceof Long) {
                        result = (Long) result;
                    } else if (result instanceof BigInteger) {
                        result = ((BigInteger) result).longValue();
                    } else if (result instanceof BigDecimal) {
                        // 增加oracle的uniqueResult返回类型判断
                        result = ((BigDecimal) result).longValue();
                    }
                }
            } catch (SQLException e) {
                throw e;
            }
        }
        closeConnection(conn);
        return result;
    }

    /**
     * 执行sql语句，默认jndi "java:/datasources/visesbdb"
     * 
     * @param conn
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static boolean excuteNativeSql(String sql, Object... params) throws SQLException {
        Connection conn = getConnection();
        if (conn == null) {
            return false;
        }
        return excuteNativeSql(conn, sql, params);
    }

    /**
     * 执行批量sql语句，默认jndi "java:/datasources/visesbdb"
     * 
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static boolean executeBatchNativeSql(String sql, List<Object[]> params) throws SQLException {
        Connection conn = getConnection();
        if (conn == null) {
            return false;
        }
        return executeBatchNativeSql(conn, sql, params);
    }

    /**
     * 执行批量sql语句
     * 
     * @param conn
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static boolean executeBatchNativeSql(Connection conn, String sql, List<Object[]> params)
            throws SQLException {
        if (conn == null) {
            return false;
        }
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            int batchSize = 1000;
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    Object[] param = params.get(i);
                    for (int j = 0; j < param.length; j++) {
                        st.setObject(j + 1, param[j]);
                    }
                    st.addBatch();
                    if (i + 1 % batchSize == 0) {
                        st.executeBatch();
                    }
                }
            }
            st.executeBatch();
            st.close();
            return true;
        } catch (SQLException e) {
            throw e;
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * 执行sql查询语句，默认jndi "java:/datasources/visesbdb" 列名统一大写
     * 
     * @param sql
     * @param params
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> queryByNativeSql(String sql, Object... params) throws Exception {
        Connection conn = getConnection();
        return queryByNativeSql(conn, sql, params);
    }

    /**
     * 执行sql查询语句 列名统一大写
     * 
     * @param conn
     * @param sql
     * @param params
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> queryByNativeSql(Connection conn, String sql, Object... params)
            throws Exception {
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
                    if (value instanceof CLOB) {
                        value = transClobToString((CLOB) value);
                    } else if (value instanceof BLOB) {
                        value = transBlobToString((BLOB) value);
                    }
                    map.put(rsm.getColumnLabel(col + 1).toUpperCase(), value);
                }
                resultList.add(map);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (final SQLException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            closeConnection(conn);
        }

        return resultList;
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
     * 通过jndi执行sql语句
     * 
     * @param jndi
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static boolean excuteNativeSqlByJndi(String jndi, String sql, Object... params) throws SQLException {
        Connection conn = getConnection(jndi);
        if (conn == null) {
            return false;
        }
        return excuteNativeSql(conn, sql, params);
    }

    /**
     * 执行sql语句
     * 
     * @param conn
     * @param sql
     * @param params
     * @return
     * @throws SQLException
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
            st.close();
            return true;
        } catch (SQLException e) {
            throw e;
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * 执行批量oracle sql语句
     * 
     * @param sql
     * @param params
     * @return
     * @throws Exception
     */
    public static boolean executeBatchNativeSqlhasBlob(String sql, List<Object[]> params) throws Exception {
        Connection conn = getConnection();
        if (conn == null) {
            return false;
        }
        try {
            conn = ((DelegatingConnection) getConnection().getMetaData().getConnection()).getDelegate();
            PreparedStatement st = conn.prepareStatement(sql);
            int batchSize = 1000;
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    Object[] param = params.get(i);
                    for (int j = 0; j < param.length; j++) {
                        if (param[j] instanceof byte[]) {
                            st.setBlob(j + 1, getBlobObject(conn, param[j]));

                        } else {
                            st.setObject(j + 1, param[j]);
                        }
                    }
                    st.addBatch();
                    if (i + 1 % batchSize == 0) {
                        st.executeBatch();
                    }
                }
            }
            st.executeBatch();
            st.close();
            return true;
        } catch (SQLException e) {
            // logger.error("executeBatch has error ...",e);
            throw e;
        } finally {
            closeConnection(conn);
        }
    }

    private static Blob getBlobObject(Connection conn, Object value) throws Exception {
        BLOB blob = null;
        OutputStream output = null;
        try {
            blob = BLOB.createTemporary(conn, true, BLOB.DURATION_SESSION);
            output = blob.setBinaryStream(0);
            if (value != null) {
                byte[] data = (byte[]) value;
                output.write(data, 0, data.length);
                output.flush();
            }
        } catch (Exception e) {
            // logger.error("executeBatch getBlobObject error ...",e);
            throw e;
        } finally {
            try {
                if (output != null)
                    output.close();
            } catch (IOException e) {
                logger.error("executeBatch OutputStream close error ...", e);
            }
        }
        return blob;
    }

}

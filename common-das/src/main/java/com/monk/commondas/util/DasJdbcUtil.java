package com.monk.commondas.util;

import oracle.sql.BLOB;
import oracle.sql.CLOB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DasJdbcUtil {
    
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(DasJdbcUtil.class);
    
    /**
     * 执行查询SQL
     * 
     * 慎用，当前方法执行完SQL后不会立马关闭连接
     * 
     * @param conn
     *            连接对象
     * @param sql
     *            sql语句
     * @param params
     *            查询条件
     * @return 查询结果
     * @throws Exception
     *             执行异常
     * @author Monk
     * @date 2020年8月7日 下午8:06:15
     */
    public static List<Map<String, Object>> queryByNativeSql(Connection conn, String sql, Object... params) throws Exception {
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
                    Object value = rs.getObject(col + 1);
                    if (value instanceof CLOB) {
                        value = JDBCUtil.transClobToString((CLOB) value);
                    } else if (value instanceof BLOB) {
                        value = JDBCUtil.transBlobToString((BLOB) value);
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
        }

        return resultList;
    }
    
    
    /**
     * 通过查询sql获取结果集中第一行，第一列的值
     * 
     * @param conn
     * @param query
     * @return
     * @throws SQLException
     */
    public static Object queryUniqueResult(Connection conn, String query, Object... params) throws SQLException {
        Object result = null;
        if (conn != null) {
            try {
                PreparedStatement st = conn.prepareStatement(query);
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        st.setObject(i + 1, params[i]);
                    }
                }
                ResultSet rs = st.executeQuery();
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
        return result;
    }
    
    /**
     * 执行SQL
     * 
     * 慎用，当前方法执行完SQL后不会立马关闭连接
     * 
     * @param sql
     *            SQL语句
     * @param params
     *            SQL参数
     * @throws SQLException
     * @author Monk
     * @date 2020年8月7日 下午8:40:34
     */
    public static void executeBatchNativeSql(Connection conn, String sql, Object... params) throws SQLException {
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    st.setObject(i + 1, params[i]);
                }
            }
            st.execute();
            st.close();
        } catch (SQLException e) {
            throw e;
        }
    }

}

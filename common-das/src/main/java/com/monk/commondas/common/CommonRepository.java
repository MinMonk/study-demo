package com.monk.commondas.common;

import com.monk.commondas.util.JDBCUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公共的数据库操作类
 * 
 * <pre>
 * 注意：当前类下的查询方法和执行SQL方法，最终均未关闭数据库Connection连接对象，需要手动关闭<br/>
 * 这么做的原因主要是为了保证接口中数据入库的全局事务回滚
 * </pre>
 * 
 * @author Monk
 * @version V1.0
 * @date 2020年5月16日 下午4:49:31
 */
public class CommonRepository {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(CommonRepository.class);

    /**
     * 非空判断 （null 或者 isBlank抛出IllegalArgumentException异常）
     * 
     * @param object
     *            判断的对象
     * @param message
     *            为空的错误消息
     * @author Monk
     * @date 2020年5月15日 下午3:14:18
     */
    public static void NotBlank(Object object, String message) {
        if (null == object || StringUtils.isBlank(object.toString().trim())) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 获取数据库连接
     * 
     * @return
     * @author Monk
     * @date 2020年5月15日 下午3:13:21
     */
    public static Connection getConnection() {
        return JDBCUtil.getConnection();
    }

    public static Long exeCountQuery(String sql, Object... params) throws SQLException {
        Connection conn = getConnection();
        List<Map<String, Object>> result = execQuery(conn, sql, params);
        if (result != null && result.size() > 0) {
            Map<String, Object> row = result.get(0);
            Object value = row.getOrDefault("TOTAL", 0L);
            Long total = 0L;
            if (value instanceof Long) {
                total = (Long) value;
            } else if (value instanceof BigInteger) {
                total = ((BigInteger) value).longValue();
            } else if (value instanceof BigDecimal) {
                // 增加oracle的uniqueResult返回类型判断
                total = ((BigDecimal) value).longValue();
            }
            return total;
        } else {
            return 0L;
        }
    }

    /**
     * 执行查询语句
     * 
     * @param conn
     *            获取连接对象
     * @param sql
     *            查询sql
     * @param params
     *            查询条件
     * @return 查询结果
     * @throws Exception
     *             异常
     * @author Monk
     * @date 2020年5月15日 下午3:15:52
     */
    public static List<Map<String, Object>> execQuery(Connection conn, String sql, Object... params)
            throws SQLException {
        if (conn != null) {
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
                        map.put(rsm.getColumnLabel(col + 1), value);
                    }
                    resultList.add(map);
                }
                return resultList;
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
        } else {
            throw new RuntimeException("DataBase Connection is null");
        }
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
    public static boolean excuteNativeSql(Connection conn, String sql, List<Object[]> params) throws SQLException {
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
        }
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
}

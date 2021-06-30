/**
 * 
 * 文件名：CreateEntity.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.common.utils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 根据数据库表结构生成java实体类，过滤适配字段ATTRIBUTE*
 * 
 * @author Monk
 * @version V1.0
 * @date 2018年11月27日 下午2:57:47
 */
public class CreateEntity {
    
    private final static String FLAG_Y = "Y";
    private final static String FLAG_N= "N";
    private final static String FLAG_TRUE= "true";
    private final static String FLAG_FALSE= "false";
    private final static String TABLE_TYPE= "TABLE";
    private final static String TABLE_TYPE_VIEW= "VIEW";
    
    private static Logger logger = LoggerFactory.getLogger(CreateEntity.class);
    
    public static void main(String[] args) {
        String tableName = "SOA_ESB_POINT_CONFIG,SOA_ESB_POINT_FIELD,SOA_ESB_POINT_FIELD_LINK,SOA_ESB_POINT_LOOKUP,SOA_ESB_POINT_SERVICE_CONFIG";
        String filePath = "D:\\entity\\pointTopoint";
        String tableType = "";
        Connection conn = JdbcUtils.getConnection();
        
        try {
            if (tableName.contains(",")) {
                for (String str : tableName.split(",")) {
                    generateJavaEntity(conn, str.trim().toUpperCase(), filePath, tableType);
                    logger.info("===============================================================");
                }
            } else {
                generateJavaEntity(conn, tableName.toUpperCase(), filePath, tableType);
            } 
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (null != conn) {
                    conn.close();
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 生产java实体Bean
     * 
     * @param tableName
     *            表明
     * @param dirPath
     *            生产的文件路径
     * @author Monk
     * @date 2018年11月27日 下午4:26:05
     */
    public static void generateJavaEntity(Connection conn, String tableName, String dirPath, String type) {
        if(null == type || "".equals(type.trim())) {
            type = TABLE_TYPE;
        }
        if(tableName.endsWith("_V")) {
            type = TABLE_TYPE_VIEW;
        }
        logger.info("table type is [{}]", type );
        
        StringBuilder sqlBuilder = new StringBuilder();
        if(type.equalsIgnoreCase(TABLE_TYPE)) {
            sqlBuilder.append("SELECT A.COLUMN_NAME, A.DATA_TYPE, A.NULLABLE, B.COMMENTS, ");
            sqlBuilder.append("(CASE WHEN A.COLUMN_NAME = CU.COLUMN_NAME THEN 'Y' ELSE 'N' END) AS IS_PK ");
            sqlBuilder.append("FROM USER_TAB_COLS A LEFT JOIN USER_COL_COMMENTS B ");
            sqlBuilder.append("ON A.TABLE_NAME = B.TABLE_NAME AND A.COLUMN_NAME = B.COLUMN_NAME ");
            sqlBuilder.append("LEFT JOIN USER_CONSTRAINTS AU ON A.TABLE_NAME = AU.TABLE_NAME ");
            sqlBuilder.append("LEFT JOIN USER_CONS_COLUMNS CU ON AU.CONSTRAINT_NAME = CU.CONSTRAINT_NAME ");
            sqlBuilder.append("WHERE AU.CONSTRAINT_TYPE = 'P' ");
        }else if(type.equalsIgnoreCase(TABLE_TYPE_VIEW)) {
            sqlBuilder.append("SELECT A.COLUMN_NAME, A.DATA_TYPE, A.NULLABLE, B.COMMENTS, ");
            sqlBuilder.append("'N' AS IS_PK FROM USER_TAB_COLS A LEFT JOIN USER_COL_COMMENTS B ");
            sqlBuilder.append("ON A.TABLE_NAME = B.TABLE_NAME AND A.COLUMN_NAME = B.COLUMN_NAME ");
            sqlBuilder.append("WHERE 1 = 1 ");
        }else {
            logger.warn("暂不支持此类型的数据库实体创建");
            return;
        }
        sqlBuilder.append("AND A.TABLE_NAME = '"); 
        sqlBuilder.append(tableName.toUpperCase());
        sqlBuilder.append("' ");
        
        logger.info("query sql : [{}]", sqlBuilder.toString());
        
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conn.createStatement();
            rs = stm.executeQuery(sqlBuilder.toString());
            StringBuffer content = initJavaBean(tableName);
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                String dataType = rs.getString("DATA_TYPE");
                String comments = rs.getString("COMMENTS");
                String isPK = rs.getString("IS_PK");
                String nullAble = rs.getString("NULLABLE");
                if(nullAble.equals(FLAG_Y)) {
                    nullAble = FLAG_TRUE;
                }else if(nullAble.equals(FLAG_N)) {
                    nullAble = FLAG_FALSE;
                }
                if (columnName.startsWith("ATTRIBUTE")) {
                    continue;
                }
                content.append(generateJavaFields(tableName, columnName, dataType, comments, isPK, nullAble));
            }
            content.append("}");
            String path = dirPath + "\\" + StringUtils.formatClassName(tableName.replace("SOA_", "")) + ".java";
            FileUtils.writeStringToFile(new File(path), content.toString());

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (null != stm) {
                    stm.close();
                }
                if (null != rs) {
                    rs.close();
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 生成java实体类的字段属性
     * 
     * @param columnName
     *            数据库列名
     * @param dataType
     *            数据库字断类型
     * @param comments
     *            数据库字断注释
     * @return
     * @author Monk
     * @date 2018年11月27日 下午4:19:37
     */
    public static String generateJavaFields(String tableName, String columnName, String dataType, String comments, String isPK, String nullAble) {
        StringBuffer buffer = new StringBuffer();
        String type = "";
        if ("VARCHAR2".equals(dataType)) {
            type = "String";
        } else if ("NUMBER".equals(dataType)) {
            type = "Long";
        } else {
            type = StringUtils.FirstToUpperCase(dataType);
        }
        if(org.apache.commons.lang.StringUtils.isNotBlank(comments)) {
            buffer.append("/** " + comments + " */\r\n");
        }
        if(isPK.equals(FLAG_Y)) {
            buffer.append("@Id\r\n");
            buffer.append("@SequenceGenerator(name = \"" + tableName +"_S\", sequenceName = \"" + tableName +"_S\", allocationSize = 1)\r\n");
            buffer.append("@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = \"" + tableName +"_S\")\r\n");
        }
        if(nullAble.equals("false")) {
            buffer.append("@Column(name = \"" + columnName + "\", nullable = " + nullAble + ")\r\n");
        }else {
            buffer.append("@Column(name = \"" + columnName + "\")\r\n");
        }
        buffer.append("private " + type + " " + StringUtils.formartField(columnName) + ";");
        buffer.append("\r\n\n");

        return buffer.toString();
    }

    /**
     * java Bean头部信息初始化
     * 
     * @param tableName
     * @return
     * @author Monk
     * @date 2018年11月27日 下午4:31:06
     */
    public static StringBuffer initJavaBean(String tableName) {
        StringBuffer content = new StringBuffer();
        content.append("@Entity\r\n");
        content.append("@Table(name = \"" + tableName + "\")\r\n");
        content.append("public class " + StringUtils.formatClassName(tableName.replace("SOA_", ""))
                + " implements Serializable{\r\n\n");
        return content;
    }
}

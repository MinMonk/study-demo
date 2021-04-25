/**
 * 
 * 文件名：Test6.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.demo;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.monk.common.utils.JdbcUtils;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2019年6月25日 下午2:58:12
 */
public class DatabaseMetaDataDemo {
    public static void main(String[] args) {
        printTableInfo("zyh", 5, 1);
//        printTableInfo(5, 2);
//        printTableInfo(5, 3);
        
        //pageAllTableColumn("SOA_SYS_USER", 5, 3);
        
    }
    
    public static void printTableInfo(String tableNamePattern, int pageSize, int pageNum) {
        Page page =  pageAllTable(tableNamePattern, pageSize, pageNum);
        System.out.println("第" + pageNum + "页，总行数" + page.getTotal());
        for(int i = 0; i < page.getRows().size(); i++) {
            System.out.println(page.getRows().get(i));
        }
    }
    
    public static Page pageAllTable(String tableNamePattern, int pageSize, int pageNum) {
        List<Map<String, Object>> queryList = new ArrayList<Map<String, Object>>();
        Page page = new Page();
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            DatabaseMetaData dbmd = conn.getMetaData();
            if(StringUtils.isBlank(tableNamePattern)) {
                tableNamePattern = "%";
            }else {
                tableNamePattern = "%" + tableNamePattern + "%";
            }
            /**
             * 参数说明：
             * catalog:目录名称，一般都为空
             * schema:数据库名，对于oracle来说就用户名
             * tableNamePattern:表名称，支持模糊查询，%和_连个占位符，和在数据库中占位符意义一样
             * type :表的类型(TABLE | VIEW)
             */
            //rs = dbmd.getTables(null, "CSBDEVW", tableNamePattern, new String[] {"TABLE", "VIEW"});
            rs = dbmd.getTables("", null, null, new String[] {"TABLE", "VIEW"});
            List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
            while(rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("table_name", rs.getString("table_name"));
                map.put("table_type", rs.getString("table_type"));
                map.put("remarks", rs.getString("remarks"));
                tempList.add(map);
//                System.out.println(map);
            }
            
          //设置总行数
            int totalRows = tempList.size();
            page.setTotal(totalRows);
            
            int beginIndex = (pageNum - 1) * pageSize;
            int endIndex = pageNum * pageSize;
            
            for (int i = 0; i < tempList.size(); i++) {
                if(i >= endIndex) {
                    break;
                }
                if(i < beginIndex) {
                    continue;
                }
                queryList.add(tempList.get(i));
            }
            page.setRows(queryList);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != conn) {
                    conn.close();
                }
                if (null != rs) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return page;
    }
    
    public static void pageAllTableColumn(String tableNamePattern, int pageSize, int pageNum) {
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            DatabaseMetaData dbmd = conn.getMetaData();
            rs = dbmd.getColumns(null, "CSBDEVW", tableNamePattern.toUpperCase(), "%");
            List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
			ResultSetMetaData meta = rs.getMetaData();
            for(int i = 0; i < meta.getColumnCount(); i++) {
                System.out.println(meta.getColumnName(i) + ":" + rs.getObject(i));
            }
            while(rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("COLUMN_NAME", rs.getString("COLUMN_NAME"));
                map.put("DATA_TYPE", rs.getString("DATA_TYPE"));
                map.put("TYPE_NAME", rs.getString("TYPE_NAME"));
                map.put("COLUMN_SIZE", rs.getString("COLUMN_SIZE"));
                map.put("NULLABLE", rs.getString("NULLABLE"));
                tempList.add(map);
                System.out.println(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != conn) {
                    conn.close();
                }
                if (null != rs) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

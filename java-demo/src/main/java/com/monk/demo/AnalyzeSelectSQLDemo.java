/**
 * 
 * 文件名：Test5.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.demo;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.monk.common.utils.ObjectUtils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2019年6月24日 下午9:43:50
 */
public class AnalyzeSelectSQLDemo {
    public static void main(String[] args) {
       String sql1 = "select * from soa_sys_user";
       List<String> column1 = analyzeSelectSQL(sql1);
       System.out.println("sql1的解析结果如下：");
       for (String str : column1) {
           System.out.print(str + ", ");
       }
       
       System.out.println("");
       System.out.println("sql2的解析结果如下：");
       String sql2 = "select user_id, user_name as userName, email, weixin, to_date(sysdate, 'yyyy-mm-dd hh24:mi:ss') from soa_sys_user";
       List<String> column2 = analyzeSelectSQL(sql2);
       for (String str : column2) {
           System.out.print(str + ", ");
       }
    }
    
    
    public static List<String> analyzeSelectSQL(String sql){
        List<String> result = new ArrayList<String>();
        try {
            CCJSqlParserManager pm = new CCJSqlParserManager();
            Statement statement = pm.parse(new StringReader(sql));
            if (statement instanceof Select) {
                Select selectStatement = (Select) statement;
                PlainSelect selectBody = (PlainSelect) selectStatement
                        .getSelectBody();
                List<SelectItem> selectItemlist = selectBody.getSelectItems();
                SelectItem selectItem = null;
                SelectExpressionItem selectExpressionItem = null;
                AllTableColumns allTableColumns = null;
                Alias alias = null;
                SimpleNode node = null;
                if (selectItemlist != null) {
                    for (int i = 0; i < selectItemlist.size(); i++) {
                        selectItem = selectItemlist.get(i);
                        if (selectItem instanceof SelectExpressionItem) {
                            selectExpressionItem = (SelectExpressionItem) selectItemlist
                                    .get(i);
                            alias = selectExpressionItem.getAlias();
                            node = selectExpressionItem.getExpression()
                                    .getASTNode();
                            Object value = node.jjtGetValue();
                            String columnName = "";
                            if (value instanceof Column) {
                                columnName = ((Column) value).getColumnName();
                            } else if (value instanceof Function) {
                                columnName = ((Function) value).toString();
                            }else {
                                // 增加对select 'aaa' from table; 的支持
                                columnName = ObjectUtils.praseObjectToString(value);
                                columnName = columnName.replace("'", "");
                                columnName = columnName.replace("\"", "");
                            }
                            if (alias != null) {
                                columnName = alias.getName();
                            }
                            result.add(columnName);
                        } else if (selectItem instanceof AllTableColumns) {
                            allTableColumns = (AllTableColumns) selectItemlist
                                    .get(i);
                            result.add(allTableColumns.toString());
                        } else {
                            result.add(selectItem.toString());
                        }

                    }
                }
            }
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
        return result;
    }
}

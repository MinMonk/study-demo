package com.monk.common.jaxb;

import com.monk.common.jaxb.bean.DataObject;
import com.monk.common.jaxb.bean.Field;
import com.monk.common.jaxb.bean.Key;
import com.monk.common.jaxb.bean.Table;
import com.monk.common.jaxb.cache.TableConfig;
import com.monk.common.jaxb.cache.DataConfig;
import com.monk.common.jaxb.cache.DataConfigCache;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLGenerator {

    public static void main(String[] args) throws Exception {
        DataObject dataObject = TestJAXB.unMarshal();
        SQLGenerator sqlGenerator = new SQLGenerator();
        // 解析配置文件
        DataConfig dataConfig = sqlGenerator.resolveConfig(dataObject, "test_service", "0","1");

        // 生成SQL
        //buildSQL(dataConfig);
        System.out.println(" a, , a ".trim());


    }

    public static void buildSQL(DataConfig dataConfig){
        String selectSqlFormart = "select %s from %s";
        String insertSqlFormart = "insert into %s (%s) values (%s)";
        String updateSqlFormart = "update %s ";
        String deleteSqlFormart = "delete from %s";
        System.out.println(String.format(selectSqlFormart, "a", "aa"));
        System.out.println(String.format(insertSqlFormart, "a", "b", "aa"));
        System.out.println(String.format(updateSqlFormart, "a", "set a=b where a>1"));
        System.out.println(String.format(deleteSqlFormart, "a"));
    }

    public DataConfig resolveConfig(DataObject dataObject, String serviceNameEn, String majorVersion, String configVersion) throws Exception {
        String cacheKey = DataConfigCache.buildCacheKey(serviceNameEn, majorVersion);

        DataConfig dataConfig = new DataConfig();
        dataConfig.setServiceNameEn(serviceNameEn);
        dataConfig.setMajorVersion(majorVersion);
        dataConfig.setConfigVersion(configVersion);
        dataConfig.setDataSourceName(dataObject.getDataSourceName());
        dataConfig.setOperationType(dataObject.getOperationType());
        dataConfig.setMaster(resoleveTableInfo(dataObject.getMaster(), dataObject.getOperationType()));
        dataConfig.setSlaves(resolveSlaveTable(dataObject.getMaster().getSlaveTables(), dataObject.getOperationType()));
        DataConfigCache.put(cacheKey, dataConfig);

        System.out.println(dataConfig);
        return dataConfig;
    }

    public List<TableConfig> resolveSlaveTable(List<Table> tableList, String operationType){
        List<TableConfig> tableConfigList = new ArrayList<TableConfig>();
        if(CollectionUtils.isNotEmpty(tableList)) {
            for (Table table : tableList) {
                TableConfig config = resoleveTableInfo(table, operationType);
                tableConfigList.add(config);
            }
        }

        return tableConfigList;
    }

    public TableConfig resoleveTableInfo(Table table, String operationType){
        TableConfig tableConfig = new TableConfig();
        tableConfig.setEntityName(table.getEntityName());
        tableConfig.setTableName(table.getTableName());
        tableConfig.setTableType(table.getTableType());

        List<Field> fieldList = table.getFields();
        List<Field> resultList = new ArrayList<Field>();
        List<Field> importList = new ArrayList<Field>();
        List<Field> deleteList = new ArrayList<Field>();
        List<Field> percizeList = new ArrayList<Field>();
        List<Field> fuzzyList = new ArrayList<Field>();
        if(CollectionUtils.isNotEmpty(fieldList)){
            for(Field field : fieldList){
                if(field.getPrecise()){
                    percizeList.add(field);
                }
                if(field.getFuzzy()){
                    fuzzyList.add(field);
                }
                if(field.getResult()){
                    resultList.add(field);
                }
                if(field.getImport()){
                    importList.add(field);
                }
                if(field.getDelete()){
                    deleteList.add(field);
                }
            }
        }
        tableConfig.setPercizeList(percizeList);
        tableConfig.setFuzzyList(fuzzyList);
        tableConfig.setResultList(resultList);
        tableConfig.setImportList(importList);
        tableConfig.setDeleteList(deleteList);

        List<Map<String, String>> mappingList = new ArrayList<Map<String, String>>();
        List<Key> keyList = table.getKeys();
        if(CollectionUtils.isNotEmpty(keyList)) {
            for (Key key : keyList) {
                Map<String, String> mapping = new HashMap<String, String>();
                mapping.put(key.getParentField(), key.getChildField());
                mappingList.add(mapping);
            }
        }
        tableConfig.setMappingList(mappingList);
        return tableConfig;
    }
}

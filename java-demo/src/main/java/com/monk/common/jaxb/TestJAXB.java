package com.monk.common.jaxb;

import com.monk.common.jaxb.bean.DataObject;
import com.monk.common.jaxb.bean.Field;
import com.monk.common.jaxb.bean.Key;
import com.monk.common.jaxb.bean.Table;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TestJAXB {

    private static final String filePath = TestJAXB.class.getClass().getResource("/").getPath() + "dataobject.xml";

    public static void main(String[] args) throws Exception {
        marshal();
        System.out.println("------------------------------");
        //unMarshal();
    }

    private static List<Field> buildFiledInfo(String fieldStr) {
        String[] fields = fieldStr.split(",");
        List<Field> fieldInfos = new ArrayList<>();
        for (String str : fields) {
            str = str.trim();
            Field field = new Field();
            field.setFieldName(str.toLowerCase());
            field.setColumnName(str.toUpperCase());
            field.setColumnType("VARCHAR2");
            field.setResult(true);
            field.setPrecise(true);
            field.setFuzzy(false);
            field.setImport(true);
            field.setDelete(true);
            fieldInfos.add(field);
        }
        return fieldInfos;
    }

    private static List<Key> buildKeyInfo(String keyStr) {
        String[] keys = keyStr.split(",");
        List<Key> keyInfos = new ArrayList<>();
        for (String key : keys) {
            key = key.trim();
            Key keyInfo = new Key();
            keyInfo.setParentField(key);
            keyInfo.setChildField(key);
            keyInfos.add(keyInfo);
        }
        return keyInfos;
    }

    private static Table buildTableInfo(String tableName, String fieldStr, String keyStr) {
        Table table = new Table();
        table.setEntityName(tableName.replaceAll("DB_ADAPTER_TEST_", "").toLowerCase());
        table.setTableName(tableName);
        table.setTableType("TABLE");
        table.setFields(buildFiledInfo(fieldStr));
        if(StringUtils.isNotBlank(keyStr)){
            table.setKeys(buildKeyInfo(keyStr));
        }
        return table;
    }

    private static Table buildMasterTable(String tableName, String fieldStr) {
        List<Table> slaveTables = new ArrayList<>();
        Table classInfo = buildTableInfo("DB_ADAPTER_TEST_CLASS", "CLASS_ID, CLASS_NO, CLASS_NAME", "CLASS_ID, CLASS_NO");
        Table provinceInfo = buildTableInfo("DB_ADAPTER_TEST_PROVINCE", "PROVINCE_ID, PROVINCE_NAME, SIMPLE_NAME", "PROVINCE_ID");
        slaveTables.add(classInfo);
        slaveTables.add(provinceInfo);

        Table master = buildTableInfo("DB_ADAPTER_TEST_USER", "ID, NAME, CLASS_ID, PROVINCE_ID", null);
        master.setSlaveTables(slaveTables);
        return master;
    }

    private static DataObject initData() {
        DataObject dataObject = new DataObject();
        dataObject.setDataObjectName("QUERY_USER_INFO");
        dataObject.setDataSourceName("test_datasource");
        dataObject.setOperationType("SELECT");
        dataObject.setMaster(buildMasterTable("DB_ADPATER_TEST_USER", "ID, NAME, CLASS_ID, PROVINCE_ID"));
        return dataObject;
    }

    public static void marshal() throws Exception {
        //Student student = new Student(1L, "jack");
        DataObject dataObject = initData();
        JAXBContext context = createJAXBContext();
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        FileOutputStream fileStream = new FileOutputStream(filePath);
        marshaller.marshal(dataObject, fileStream);

        try {
            File file = new File(filePath);
            List<String> lines = FileUtils.readLines(file, "UTF-8");
            lines.forEach(line -> {
                System.out.println(line);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DataObject unMarshal() throws Exception {
        JAXBContext context = createJAXBContext();
        Unmarshaller unmarshal = context.createUnmarshaller();
        InputStream stream = new FileInputStream(new File(filePath));
        DataObject dataObject = (DataObject) unmarshal.unmarshal(stream);
        System.out.println(dataObject);
        return dataObject;
    }

    public static JAXBContext createJAXBContext() {
        try {
            return JAXBContext.newInstance(DataObject.class);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }
}

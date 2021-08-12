package com.monk.test.zookeeper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.zookeeper.CreateMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.monk.zookeeper.ZkOperationDemo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OperationTest {
    
    @Autowired
    private ZkOperationDemo dmeo;
    
    @Test
    public void createNodeTest() {
        dmeo.createNode("/demo1", "123");
    }
    
    @Test
    public void createNodeTest2() {
        dmeo.createNode("/demo2", "123", 1000L);
    }
    
    @Test
    public void createNodeTest3() {
        dmeo.createNode("/demo3", "123", CreateMode.PERSISTENT);
    }
    
    @Test
    public void createNodeTest4() {
        dmeo.createNode("/demo4", "123", CreateMode.EPHEMERAL);
        dmeo.checkExist("/demo4");
    }

    
    @Test
    public void setValue() {
        dmeo.setValue("/demo1", "12345");
    }
    
    @Test
    public void test() {
        
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String[] tableNames = new String[] {"SOA_ESB_CPU_INFO_HIS","SOA_ESB_DISK_INFO_HIS","SOA_ESB_JVM_GCUTIL_INFO_HIS"};
        
        // alter table SOA_ESB_CPU_INFO_HIS add partition P20210806 values less than(TO_DATE('2021-08-06 00:00:00', 'YYYY-MM-DD HH24:MI:SS'))
        StringBuilder sql = new StringBuilder();
        for (String tableName : tableNames) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DATE, 5);
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            for(int i = 0; i < 60; i++) {
                cal.add(Calendar.DATE, 1);
                String datestr = sf.format(cal.getTime());
                sql.append("ALTER TABLE ").append(tableName).append(" ADD PARTITION P");
                sql.append(datestr.substring(0, 10).replaceAll("-", "").trim());
                sql.append(" values less than(TO_DATE('");
                sql.append(datestr).append("', 'YYYY-MM-DD HH24:MI:SS'));").append("\r\n");
            }
        }
        System.out.println(sql);
    }
    
    
}

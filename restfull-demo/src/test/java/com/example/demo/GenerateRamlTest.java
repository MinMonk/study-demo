package com.example.demo;


import com.alibaba.fastjson.JSON;
import com.example.demo.entity.EsbService;
import com.example.demo.entity.EsbServiceParameter;
import com.example.demo.service.AbstractRamlGenerator;
import com.example.demo.service.ServiceRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GenerateRamlTest {

    @Autowired
    AbstractRamlGenerator generator;

    @Autowired
    private ServiceRepository serviceRepository;

    @Test
    public void generatorFile(){
        EsbService service =  serviceRepository.queryService(1334L);
        System.out.println(JSON.toJSONString(service));

        generator.generateRaml(service);
    }
}

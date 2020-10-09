package com.xuecheng.manage_cms;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.service.SysDicthinaryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @Author wzg
 * @Date 2020/7/13
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestTemplate {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    SysDicthinaryService sysDicthinaryService;

    @Test
    public void testRestTemplate(){
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/%7Bid%7D?id=5a791725dd573c3574ee333f", Map.class);
        Map body = forEntity.getBody();
        System.out.println(body);
    }
    @Test
    public void test(){
        SysDictionary sysDictionary = sysDicthinaryService.getByType("200");
        System.out.println(sysDictionary);

    }
}

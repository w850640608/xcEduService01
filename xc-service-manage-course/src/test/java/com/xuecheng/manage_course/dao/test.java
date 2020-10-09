package com.xuecheng.manage_course.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author wzg
 * @Date 2020/8/14
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class test {
    int a = 0;
    int b = 0;
    int c =2;
    Map map = new HashMap();
        @Test
         public void te(){
            if(c==2){
                map.put("a",1);
            }else{
                map.put("b",2);
            }
            System.out.println(map.get("a"));
            System.out.println(map.get("b"));

        }


}

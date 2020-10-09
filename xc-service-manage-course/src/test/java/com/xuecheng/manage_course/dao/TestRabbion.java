package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_course.client.CmsPageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRabbion {
    @Autowired
    CmsPageClient cmsPageClient;//代理对象  尤fig


    @Test
    public void testPageHelper(){
       CmsPage cmsPage = cmsPageClient.findCmsPageByID("5a754adf6abb500ad05688d9");
       System.out.println(cmsPage);

    }
}

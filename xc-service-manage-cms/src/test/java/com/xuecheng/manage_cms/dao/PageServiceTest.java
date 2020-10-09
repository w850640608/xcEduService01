package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsPageParam;
import com.xuecheng.manage_cms.service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author wzg
 * @Date 2020/6/19
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class PageServiceTest {
    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    PageService pageService;
    //分页测试
    @Test
    public void testFindPage() {
        String html=pageService.getPageHtml("5f0aabc732a3b31d9cfb1da9");
        System.out.println(html);
    }
    //添加
    @Test public void testInsert(){
        //定义实体类
       CmsPage cmsPage = new CmsPage();
       cmsPage.setSiteId("s01");
       cmsPage.setTemplateId("t01");
       cmsPage.setPageName("测试页面");
       cmsPage.setPageCreateTime(new Date());
       List<CmsPageParam> cmsPageParams = new ArrayList<>();
       CmsPageParam cmsPageParam = new CmsPageParam();
       cmsPageParam.setPageParamName("param1");
       cmsPageParam.setPageParamValue("value1");
       cmsPageParams.add(cmsPageParam);
       cmsPage.setPageParams(cmsPageParams);
       cmsPageRepository.save(cmsPage);
       System.out.println(cmsPage);
    }
    //删除
    @Test public void testDelete() {
        cmsPageRepository.deleteById("5b17a2c511fe5e0c409e5eb3");
    }
    /*//修改
    @Test public void testUpdate() {
        Optional<CmsPage> optional = cmsPageRepository.findOne("s");
        if(optional.isPresent()){
            CmsPage cmsPage = optional.get();
            cmsPage.setPageName("测试页面01");
            cmsPageRepository.save(cmsPage);
        }
    }*/
}

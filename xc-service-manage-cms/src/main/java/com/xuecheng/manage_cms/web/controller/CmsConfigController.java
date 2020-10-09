package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.api.cms.CmsConfigControllerApi;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author wzg
 * @Date 2020/7/13
 * @Version 1.0
 */
@RestController
@RequestMapping("/cms/config")
public class CmsConfigController implements CmsConfigControllerApi {
    @Autowired
    PageService pageService;
    @Override
    @GetMapping("/getmodel/{id}")
    public CmsConfig getmodel(String id) {
        return pageService.getConfigById(id);
    }
}

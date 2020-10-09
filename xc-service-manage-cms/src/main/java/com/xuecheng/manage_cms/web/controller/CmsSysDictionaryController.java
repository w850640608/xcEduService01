package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.api.cms.SysDicthinaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.service.SysDicthinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author wzg
 * @Date 2020/7/29
 * @Version 1.0
 */
@RestController
@RequestMapping("/sys")
public class CmsSysDictionaryController implements SysDicthinaryControllerApi {
    @Autowired
    SysDicthinaryService sysDicthinaryService;
    @GetMapping("/dictionary/get/{type}")
    public SysDictionary getByType(@PathVariable String type) {

        return sysDicthinaryService.getByType(type);
    }
}

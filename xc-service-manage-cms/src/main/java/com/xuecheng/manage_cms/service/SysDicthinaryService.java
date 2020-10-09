package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.dao.CmsSysDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author wzg
 * @Date 2020/7/29
 * @Version 1.0
 */
@Service
public class SysDicthinaryService {
    @Autowired
    CmsSysDictionaryRepository cmsSysDictionaryRepository;
    public SysDictionary getByType(String type){
        SysDictionary sysDictionary = cmsSysDictionaryRepository.findAllByDType(type);
        return sysDictionary;
    }
}

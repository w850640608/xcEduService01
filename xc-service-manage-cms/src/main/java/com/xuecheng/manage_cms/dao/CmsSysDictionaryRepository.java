package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author wzg
 * @Date 2020/7/29
 * @Version 1.0
 */
public interface CmsSysDictionaryRepository extends MongoRepository<SysDictionary,String> {
    SysDictionary findAllByDType(String type);
}

package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author wzg
 * @Date 2020/7/13
 * @Version 1.0
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfig,String> {
}

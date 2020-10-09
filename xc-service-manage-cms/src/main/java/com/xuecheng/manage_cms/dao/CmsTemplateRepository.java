package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author wzg
 * @Date 2020/6/19
 * @Version 1.0
 */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {
}

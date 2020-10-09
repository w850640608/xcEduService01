package com.xuecheng.framework.domain.cms.ext;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import lombok.Data;
import lombok.ToString;

/**
 * @Author wzg
 * @Date 2020/6/19
 * @Version 1.0
 */
@Data
@ToString
public class CmsTemplateExt extends CmsTemplate {

    //模版内容
    private String templateValue;

}

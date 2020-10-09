package com.xuecheng.framework.domain.cms.ext;

import com.xuecheng.framework.domain.cms.CmsPage;
import lombok.Data;
import lombok.ToString;

/**
 * @Author wzg
 * @Date 2020/6/19
 * @Version 1.0
 */
@Data
@ToString
public class CmsPageExt extends CmsPage {
    private String htmlValue;

}

package com.xuecheng.framework.domain.cms;

import lombok.Data;
import lombok.ToString;

/**
 * @Author wzg
 * @Date 2020/6/17
 * @Version 1.0
 */
@Data
@ToString
public class CmsPageParam {
   //参数名称
    private String pageParamName;
    //参数值
    private String pageParamValue;

}

package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.Teachplan;
import lombok.Data;
import lombok.ToString;

/**
 * @Author wzg
 * @Date 2020/7/23
 * @Version 1.0
 */
@Data
@ToString
public class TeachplanExt extends Teachplan {

    //媒资文件id
    private String mediaId;

    //媒资文件原始名称
    private String mediaFileOriginalName;

    //媒资文件访问地址
    private String mediaUrl;
}

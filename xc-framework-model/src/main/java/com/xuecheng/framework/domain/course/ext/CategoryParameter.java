package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.Category;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author wzg
 * @Date 2020/7/23
 * @Version 1.0
 */
@Data
@ToString
public class CategoryParameter extends Category {

    //二级分类ids
    List<String> bIds;
    //三级分类ids
    List<String> cIds;

}

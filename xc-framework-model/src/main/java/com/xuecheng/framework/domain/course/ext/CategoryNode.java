package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.Category;
import lombok.Data;
import lombok.ToString;

import java.util.List;
/**
 * @Author wzg
 * @Date 2020/7/29
 * @Version 1.0
 */
@Data
@ToString
public class CategoryNode extends Category {

    List<CategoryNode> children;

}

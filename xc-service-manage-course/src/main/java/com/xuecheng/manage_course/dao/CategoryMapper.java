package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author wzg
 * @Date 2020/7/23
 * @Version 1.0
 */
@Mapper
public interface CategoryMapper {
   CategoryNode CourseCategoryList();

}

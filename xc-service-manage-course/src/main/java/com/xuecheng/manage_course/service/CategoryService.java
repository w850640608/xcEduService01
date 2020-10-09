package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.dao.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author wzg
 * @Date 2020/7/29
 * @Version 1.0
 */
@Service
public class CategoryService {
    @Autowired
    CategoryMapper categoryMapper;
    public CategoryNode findCategoryList(){
        return categoryMapper.CourseCategoryList();
    }
}

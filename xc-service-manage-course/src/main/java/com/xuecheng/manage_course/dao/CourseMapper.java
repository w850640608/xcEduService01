package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.xuecheng.framework.domain.course.CourseBase;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author wzg
 * @Date 2020/7/23
 * @Version 1.0
 */
@Mapper
public interface CourseMapper {
   CourseBase findCourseBaseById(String id);
   Page<CourseBase> findCourseList();

}

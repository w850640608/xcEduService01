package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author wzg
 * @Date 2020/7/23
 * @Version 1.0
 */
public interface CourseBaseRepository extends JpaRepository<CourseBase,String> {
}

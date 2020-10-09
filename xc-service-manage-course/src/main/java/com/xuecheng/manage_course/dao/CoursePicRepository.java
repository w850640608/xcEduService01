package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author wzg
 * @Date 2020/8/27
 * @Version 1.0
 */
public interface CoursePicRepository extends JpaRepository<CoursePic,String> {
    long deleteByCourseid(String courseId);
}

package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author wzg
 * @Date 2020/7/24
 * @Version 1.0
 */
@Mapper
public interface TeachplanRepository extends JpaRepository<Teachplan,String> {
    //根据课程id和parentId查询
    public List<Teachplan> findByCourseidAndParentid(String courseId,String parentId);
}

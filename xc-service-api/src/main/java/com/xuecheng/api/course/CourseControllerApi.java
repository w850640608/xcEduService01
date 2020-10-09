package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author wzg
 * @Date 2020/7/23
 * @Version 1.0
 */
@Api(value = "课程管理接口",description = "cms课程管理接口,提供课程的增删改查")
public interface CourseControllerApi {
    @ApiOperation("课程计划查询")
    public TeachplanNode findTeachplanList(String courseId);
    @ApiOperation("添加课程计划")
    public ResponseResult addTeachplan(Teachplan teachplan);
    @ApiOperation("查询我的课程列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value = "页码",required=true,paramType="path",dataType="int"),
            @ApiImplicitParam(name="size",value = "每页记录数",required=true,paramType="path",dataType="int")
    })
    public QueryResponseResult<CourseInfo> findCourseList(
            int page, int size,@ApiParam(name="courseListRequest",value="条件",required=false) CourseListRequest courseListRequest
    );
    @ApiOperation("新增课程")
    public ResponseResult addCourseList(CourseBase courseBase);
    @ApiOperation("新增课程图片")
    public ResponseResult addCoursePic(String courseId,String pic);
    @ApiOperation("查询课程图片")
    public CoursePic findCoursePic(String courseId);
    @ApiOperation("删除课程图片")
    public ResponseResult deleteCoursePic(String courseId);
    @ApiOperation("课程视图查询")
    public CourseView courseview(String id);
    @ApiOperation("课程预览")
    public CoursePublishResult preview(String id);
    @ApiOperation("课程发布")
    public CoursePublishResult publish(@PathVariable String id);
}

package com.xuecheng.api.media;

import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author wzg
 * @Date 2020/10/14
 * @Version 1.0
 */
@Api(value = "媒资管理接口",description = "媒资管理接口，提供文件上传，文件处理等接口")
public interface MediaUploadControllerApi {
    //文件上传前 校验文件是否存在
    @ApiOperation("源文件上传注册")
    public ResponseResult register(String fileMd5,
                                    String fileName,
                                    Long fileSize,
                                    String mimetype,
                                    String fileExt);
    @ApiOperation("校验分块是否存在")
    public CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize);
    @ApiOperation("上传分块")
    public ResponseResult uploadchunk(MultipartFile file, Integer chunk, String fileMd5);
    @ApiOperation("合并文件")
    public ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt);

}

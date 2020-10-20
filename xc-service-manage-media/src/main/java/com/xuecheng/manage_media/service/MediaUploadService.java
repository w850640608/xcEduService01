package com.xuecheng.manage_media.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.config.RabbitMQConfig;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * @Author wzg
 * @Date 2020/10/14
 * @Version 1.0
 */
@Service
public class MediaUploadService {
    @Autowired
    MediaFileRepository mediaFileRepository;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Value("${xc-service-manage-media.upload-location}")
    String upload_location;
    @Value("${xc-service-manage-media.mq.routingkey-media-video}")
    String routingkey_media_video;
    //得到文件所属目录路径
    private String getFileFolderPath(String fileMd5){
        return upload_location+fileMd5.substring(0,1)+"/"+fileMd5.substring(1,2)+"/"+fileMd5+"/";

    }
    //得到文件路径
    private String getFilePath(String fileMd5, String fileExt){
        return upload_location+fileMd5.substring(0,1)+"/"+fileMd5.substring(1,2)+"/"+fileMd5+"/"+fileMd5+"."+fileExt;

    }
    //得到块文件所属目录路径
    private String getChunkFileFolderPath(String fileMd5){
        return upload_location+fileMd5.substring(0,1)+"/"+fileMd5.substring(1,2)+"/"+fileMd5+"/chunk/";

    }

    /**
     * 文件上传前的注册，检查文件是否存在
     * 根据文件md5得到文件路径
     * 规则：
     * 一级目录：md5的第一个字符
     * 二级目录：md5的第二个字符
     * 三级目录：md5
     * @param fileMd5 文件md5值
     * @param fileName 文件名 md5+文件扩展名
     * @param fileSize
     * @param mimetype
     * @param fileExt  文件扩展名/
     * @return 文件路径
     */
    public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        //1检查文件在磁盘上是否存在
        //文件所属目录的路径
         String fileFolderPath = this.getFileFolderPath(fileMd5);
        //文件的路径
        String filePath = this.getFilePath(fileMd5, fileExt);
        File file = new File(filePath);
        //文件是否存在
        boolean exists = file.exists();
        //2检查文件在mongodb中是否存在
        Optional<MediaFile> byId = mediaFileRepository.findById(fileMd5);
        if(exists && byId.isPresent()){
            //文件存在
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }
        //文件不存在时候做一些准备工作，检查文件目录是否存在，不存在则创建
        File file1 = new File(fileFolderPath);
        if(!file1.exists()){
            file1.mkdirs();
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 检查分块文件是否存在
     * @param fileMd5
     * @param chunk 块的下标
     * @param chunkSize 快的大小
     * @return
     */
    public CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize) {
        //检查分块文件是否存在
        //得到分块文件所属目录
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        //块文件
        File chunkFile = new File(chunkFileFolderPath+chunk);
        if(chunkFile.exists()){
            return new CheckChunkResult(CommonCode.SUCCESS,true);
        }else{
            return new CheckChunkResult(CommonCode.SUCCESS,false);
        }

    }

    /**
     *上传分块
     * @param file
     * @param chunk
     * @param fileMd5
     * @return
     */
    public ResponseResult uploadchunk(MultipartFile file, Integer chunk, String fileMd5) {
        //检查分块目录，如果不存在则要自动创建
        //得到分块目录
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        //得到分块文件路径
        String chunkFilePath = chunkFileFolderPath + chunk;

        File chunkFileFolder = new File(chunkFileFolderPath);
        //如果不存在则自动创建
        if(!chunkFileFolder.exists()){
            chunkFileFolder.mkdirs();
        }
        //得到上传文件的输入流
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
             inputStream = file.getInputStream();
            fileOutputStream = new FileOutputStream(new File(chunkFilePath));
            IOUtils.copy(inputStream,fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 合并文件
     * @param fileMd5
     * @param fileName
     * @param fileSize
     * @param mimetype
     * @param fileExt
     * @return
     */
    public ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        //1合并所有文件
        //得到分块文件的属目录
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        File chunkFileFolder = new File(chunkFileFolderPath);
        //分块文件列表
        File[] files = chunkFileFolder.listFiles();
        List<File> files1 = Arrays.asList(files);
        //创建一个合并路径
        String filePath = this.getFilePath(fileMd5, fileExt);
        File file = new File(filePath);
        //执行合并
        file = this.mergeFile(files1,file);
        if(file == null){
            //合并出错
            ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL);
        }
        //2校验文件的md5值是否和前端传入的一致
        boolean checkFileMd5 = this.checkFileMd5(file, fileMd5);
        if(!checkFileMd5){
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }

        //3将文件信息写入mongodb
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setFileOriginalName(fileName);
        mediaFile.setFileName(fileMd5+"."+fileExt);
        //文件路径保存相对路径
        String filePath1=fileMd5.substring(0,1)+"/"+fileMd5.substring(1,2)+"/"+fileMd5+"/";
        mediaFile.setFilePath(filePath1);
        mediaFile.setFileSize(fileSize);
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType(mimetype);
        mediaFile.setFileType(fileExt);
        //状态上传成功
        mediaFile.setFileStatus("301002");
        mediaFileRepository.save(mediaFile);

        sendProcessVideoMsg(mediaFile.getFileId());
        return new ResponseResult(CommonCode.SUCCESS);


    }

    /**
     * 发送视频处理消息
     * @param mediaId  媒资文件Id
     * @return
     */
    public ResponseResult sendProcessVideoMsg(String mediaId){
        //查询数据库
        Optional<MediaFile> byId = mediaFileRepository.findById(mediaId);
        if(!byId.isPresent()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //构建消息内容
        Map<String,String> map = new HashMap<>();
        map.put("mediaId",mediaId);
        String jsonString = JSON.toJSONString(map);
        //向mq发送视频处理消息

        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EX_MEDIA_PROCESSTASK,routingkey_media_video,jsonString);
        } catch (AmqpException e) {
            e.printStackTrace();
            return new ResponseResult(CommonCode.FAIL);
        }

        return new ResponseResult(CommonCode.SUCCESS);

    }
    //校验文件
    private boolean checkFileMd5(File mergeFile,String md5){
        //创建文件输入流
        try {
            FileInputStream fileInputStream = new FileInputStream(mergeFile);
            //得到文件的md5
            String md5Hex = DigestUtils.md5Hex(fileInputStream);
            //和传入的md5比较
            if(md5.equalsIgnoreCase(md5Hex)){
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }
    //合并文件
    private File mergeFile(List<File> chunkFileList,File mergeFile){
        try {
            if(mergeFile.exists()){
                mergeFile.delete();
            }else{
                    //创建一个新文件
                    mergeFile.createNewFile();
            }
            //对块文件进行排序
            Collections.sort(chunkFileList, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if(Integer.parseInt(o1.getName())>Integer.parseInt(o2.getName())){
                        return 1;
                    }
                    return -1;
                }
            });
            //创建写对象
            RandomAccessFile raf_write = new RandomAccessFile(mergeFile,"rw");
            byte[] b = new byte[1024];
            for(File chunkFile:chunkFileList){
                //创建一个读块文件的对象
                RandomAccessFile raf_read = new RandomAccessFile(chunkFile,"r");
                int len = -1;
                while((len = raf_read.read(b))!=-1){
                    raf_write.write(b,0,len);
                }
                raf_read.close();
            }
            raf_write.close();
            return mergeFile;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}

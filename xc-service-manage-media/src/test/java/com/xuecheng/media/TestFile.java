package com.xuecheng.media;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Author wzg
 * @Date 2020/10/14
 * @Version 1.0
 */
public class TestFile {
    @Autowired
    MediaFileRepository mediaFileRepository;
    @Test
    public void  te(){
        CmsPage cmsPage = new CmsPage();
        cmsPage.setTemplateId("123456");
        cmsPage.setPageName("23456");
        /*MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId("1233");
        mediaFile.setFileOriginalName("1233");
        mediaFile.setFileName("1233");
        //文件路径保存相对路径
        mediaFile.setFilePath("1233");
        long a =22;
        mediaFile.setFileSize(a);
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType("1233");
        mediaFile.setFileType("1233");
        //状态为上传成功
        mediaFile.setFileStatus("301002");
        Optional<MediaFile> b = mediaFileRepository.findById("b0e96f5452d12cf7f8d0d66642a1315c");
        System.out.println(b.isPresent());*/
        // mediaFileRepository.save(mediaFile);

    }
    //测试文件分块
    @Test
    public void testChunk() throws IOException {
        //源文件
        File sourceFile = new File("D:\\ffmpeg_test\\lucene.avi");
        //块文件目录
        String chunkFileFolder = "D:\\ffmpeg_test\\chunks\\";

        //先定义块文件大小
        long chunkFileSize = 1 * 1024 * 1024;

        //块数
        long chunkFileNum = (long) Math.ceil(sourceFile.length() * 1.0 /chunkFileSize);

        //创建读文件的对象
        RandomAccessFile raf_read = new RandomAccessFile(sourceFile,"r");

        //缓冲区
        byte[] b = new byte[1024];
        for(int i=0;i<chunkFileNum;i++){
            //块文件
            File chunkFile = new File(chunkFileFolder+i);
            //创建向块文件的写对象
            RandomAccessFile raf_write = new RandomAccessFile(chunkFile,"rw");
            int len = -1;

            while((len = raf_read.read(b))!=-1){

                raf_write.write(b,0,len);
                //如果块文件的大小达到 1M开始写下一块儿
                if(chunkFile.length()>=chunkFileSize){
                    break;
                }
            }
            raf_write.close();


        }
        raf_read.close();
    }
    //测试文件合并
    @Test
    public void testMergeFile() throws IOException {
        //块文件目录
        String chunkFileFolderPath = "D:\\ffmpeg_test\\chunks\\";
        //块文件目录对象
        File chunkFileFolder = new File(chunkFileFolderPath);
        //块文件列表
        File[] files = chunkFileFolder.listFiles();
        //将块文件排序，按名称升序
        List<File> fileList = Arrays.asList(files);
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if(Integer.parseInt(o1.getName())>Integer.parseInt(o2.getName())){
                    return 1;
                }
                return -1;

            }
        });

        //合并文件
        File mergeFile = new File("D:\\ffmpeg_test\\lucene_merge.avi");
        //创建新文件
        boolean newFile = mergeFile.createNewFile();

        //创建写对象
        RandomAccessFile raf_write = new RandomAccessFile(mergeFile,"rw");

        byte[] b = new byte[1024];
        for(File chunkFile:fileList){
            //创建一个读块文件的对象
            RandomAccessFile raf_read = new RandomAccessFile(chunkFile,"r");
            int len = -1;
            while((len = raf_read.read(b))!=-1){
                raf_write.write(b,0,len);
            }
            raf_read.close();
        }
        raf_write.close();
    }
}


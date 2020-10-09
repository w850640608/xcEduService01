package com.xuecheng.test.fastdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFastDFS {

//上传文件
    @Test
    public  void testUpload(){
        try {
            //加载fastdfs-client.properties配置文件
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            //trackerClient客户端
            TrackerClient trackerClient = new TrackerClient();
            //连接tracker
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取Stroage
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            //创建storage
            StorageClient1 storageClient1 = new StorageClient1(trackerServer,storageServer);
            //向storage上传文件
            String filePath = "d:/b.png";
            String fileId = storageClient1.upload_appender_file1(filePath,"png",null);
            System.out.println(fileId);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    //下载文件
    @Test
    public void testDown(){
        //加载fastdfs-client.properties配置文件
        try {
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            //trackerClient客户端
            TrackerClient trackerClient = new TrackerClient();
            //连接tracker
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取Stroage
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            //创建storage
            StorageClient1 storageClient1 = new StorageClient1(trackerServer,storageServer);
            String fileId = "group1/M00/00/00/wKhlg18_KtGEZvrhAAAAAONy7bw360.png";
            byte[] bytes = storageClient1.download_file1(fileId);
            //输出liu
            FileOutputStream fileOutputStream = new FileOutputStream(new File("c:/a.png"));
            fileOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

    }

}

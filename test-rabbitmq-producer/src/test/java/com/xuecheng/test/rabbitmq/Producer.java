package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author wzg
 * @Date 2020/7/15
 * @Version 1.0
 */
public class Producer {
    //队列
    private static final String QUEUE = "HELLO WORD1";
    public static void main(String[] args){
        //通过连接工厂创建新的连接和mq建立连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //设置虚拟机,一个mq服务可以设置多个虚拟机，每个虚拟机相当于一个mq
        connectionFactory.setVirtualHost("/");
        Connection connection = null;
        Channel channel = null;
        try {
             connection = connectionFactory.newConnection();
             //创建会话通道，s生产者和mq服务所有通信在通道完成
             channel = connection.createChannel();
            //声明队列 如果队列没有则创建
            channel.queueDeclare(QUEUE,true,false,false,null);
            //发送消息
            String message ="hell  小王";
            channel.basicPublish("",QUEUE,null,message.getBytes());
            System.out.println("send mq "+message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ;


        }
    }
}

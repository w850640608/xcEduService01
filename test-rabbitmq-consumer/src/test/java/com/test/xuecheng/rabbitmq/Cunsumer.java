package com.test.xuecheng.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author wzg
 * @Date 2020/7/15
 * @Version 1.0
 */
public class Cunsumer {
    //队列
    private static final String QUEUE = "HELLO WORD1";
    public static void main(String[] args) throws IOException, TimeoutException {
        //通过连接工厂创建新的连接和mq建立连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //设置虚拟机,一个mq服务可以设置多个虚拟机，每个虚拟机相当于一个mq
        connectionFactory.setVirtualHost("/");
        Connection connection = connectionFactory.newConnection();
        //创建会话通道，s生产者和mq服务所有通信在通道完成
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE,true,false,false,null);
        //实现消费方法
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            //当接收消息后此方法被调用
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
              //交换机
               String exchange = envelope.getExchange();
               //消息id mq在channel中用来标识消息的id，可用于确认消息接收
                long DeliveryTag = envelope.getDeliveryTag();
                String message = new String(body,"UTF-8");
                System.out.println(message);

            }
        };
        //监听队列
        channel.basicConsume(QUEUE,true,defaultConsumer);

    }
}

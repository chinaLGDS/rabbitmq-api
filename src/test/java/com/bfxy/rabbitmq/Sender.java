package com.bfxy.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Sender {


    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory  = new ConnectionFactory();
        connectionFactory.setHost("192.168.56.107");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //2.创建ConnectionFactory
        Connection connection = connectionFactory.newConnection();
        //3.创建channel
        Channel channel = connection.createChannel();
        // 4 声明
        String queueName = "testone";

        //参数：queue是名字，是否持久化
        channel.queueDeclare(queueName,false,false,false,null);

        Map<String, Object> headers = new HashMap<>();

        //deliveryMode 1:消息非持久化，2：消息持久化
        AMQP.BasicProperties props = new AMQP.BasicProperties().builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .headers(headers).build();
        for(int i = 0; i < 5;i++) {
            String msg = "Hello World RabbitMQ " + i;
            //channel第一没有设置exchange的名称，是按照queueName来发送消息
            //使用的是默认的exchange，props就是上面的props。
            channel.basicPublish("", queueName , props , msg.getBytes());
        }


    }
}

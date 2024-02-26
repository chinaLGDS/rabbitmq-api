package com.bfxy.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class dlxSender {
    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.56.107");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //2.创建Connection
        Connection connection = connectionFactory.newConnection();

        //3.创建Channel
        Channel channel = connection.createChannel();
        //4.声明，定义exchange名称和routingkey相关的规则
        String exchangeName = "test_dlx_exchange";
        String routingKey = "group.bfxy";
        //5.发送

        Map<String, Object> headers = new HashMap<>();
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .expiration("6000")
                .headers(headers).build();

        String msg ="hello,world RabbitMQ 4 DLX Exchange Message ...";
        //传递消息
        channel.basicPublish(exchangeName,routingKey,props,msg.getBytes());


    }
}

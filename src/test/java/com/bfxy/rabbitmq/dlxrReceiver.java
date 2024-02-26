package com.bfxy.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class dlxrReceiver {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("192.168.56.107");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);

        Connection connection = connectionFactory.newConnection();

        Channel channel =connection.createChannel();

        //声明正常的exchange1 queue 路由规则
        String queueName ="test_dlx_queue";
        String exchangeName = "test_dlx_exchange";
        String exchangeType = "topic";
        String routingKey = "group.*";
        //声明exchange
        channel.exchangeDeclare(exchangeName,exchangeType,true,false,false,null);

        //注意，这里要加一个特殊属性arguments:x-dead-letter-exchanges
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange","dlx.exchange");
        channel.queueDeclare(queueName, false, false, false, arguments);
        channel.queueBind(queueName, exchangeName, routingKey);

        //dlx declare:
        channel.exchangeDeclare("dlx.exchange", exchangeType, true, false, false, null);
        channel.queueDeclare("dlx.queue", false, false, false, null);
        channel.queueBind("dlx.queue", "dlx.exchange", "#");


        //	durable 是否持久化消息
        QueueingConsumer consumer = new QueueingConsumer(channel);
        //	参数：队列名称、是否自动ACK、Consumer
        channel.basicConsume(queueName, true, consumer);
        //	循环获取消息
        while(true){
            //	获取消息，如果没有消息，这一步将会一直阻塞
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("收到消息：" + msg);
        }

    }
}

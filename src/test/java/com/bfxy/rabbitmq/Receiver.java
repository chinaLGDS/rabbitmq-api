package com.bfxy.rabbitmq;

import com.rabbitmq.client.*;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class Receiver {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("192.168.56.107");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //服务如果宕机的话，可以进行自动切换。
        connectionFactory.setAutomaticRecoveryEnabled(true);
        //设置网络恢复间隔
        connectionFactory.setNetworkRecoveryInterval(3000);
        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        String queueName = "testone";
        // durable是否持久化消息
        channel.queueDeclare(queueName, false, false, false, null);
        QueueingConsumer consumer = new QueueingConsumer(channel);
        //参数：队列名称，是否自动ACK，Consumer,相当于设置监听
        channel.basicConsume(queueName,true,consumer);
        //循环获取消息

        while (true){
            //获取消息，如果没有消息，这一步将会一直阻塞
            Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("收到消息"+msg);
         }

    }
}

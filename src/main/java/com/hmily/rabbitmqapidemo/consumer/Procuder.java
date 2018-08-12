package com.hmily.rabbitmqapidemo.consumer;

import com.hmily.rabbitmqapidemo.common.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
public class Procuder {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(RabbitMQConfig.RABBITMQ_HOST);
        connectionFactory.setPort(RabbitMQConfig.RABBITMQ_PORT);
        connectionFactory.setVirtualHost(RabbitMQConfig.RABBITMQ_DEFAULT_VIRTUAL_HOST);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String exchange = "test_consumer_exchange";
        String routingKey = "consumer.save";
        String msg = "Hello RabbitMQ Consumer Message";
        for(int i =0; i<5; i ++){
            log.info("生产端发送：{}", msg + i);
            channel.basicPublish(exchange, routingKey, true, null, (msg + i).getBytes());
        }

    }
}

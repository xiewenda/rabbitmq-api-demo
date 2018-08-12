package com.hmily.rabbitmqapidemo.dlx;

import com.hmily.rabbitmqapidemo.common.RabbitMQConfig;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(RabbitMQConfig.RABBITMQ_HOST);
        connectionFactory.setPort(RabbitMQConfig.RABBITMQ_PORT);
        connectionFactory.setVirtualHost(RabbitMQConfig.RABBITMQ_DEFAULT_VIRTUAL_HOST);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        //注意，这只是普通的交换机和routingKey
        String exchange = "test_dlx_exchange";
        String routingKey = "dlx.save";

        for (int i = 0; i < 1; i++) {
            String msg = "Hello RabbitMQ DLX Message" + i;
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2)
                    .contentEncoding("UTF-8")
                    .expiration("10000")
                    .build();
            log.info("生产端发送：{}", msg);
            channel.basicPublish(exchange, routingKey, true, properties, msg.getBytes());
        }
    }
}

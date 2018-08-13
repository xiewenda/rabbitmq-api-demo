package com.hmily.rabbitmqapidemo.api.exchange.topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hmily.rabbitmqapidemo.common.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

@Slf4j
public class ConsumerTopicExchange {
    public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException,
            ConsumerCancelledException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(RabbitMQConfig.RABBITMQ_HOST);
        connectionFactory.setPort(RabbitMQConfig.RABBITMQ_PORT);
        connectionFactory.setVirtualHost(RabbitMQConfig.RABBITMQ_DEFAULT_VIRTUAL_HOST);
        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);
        // 声明
        String exchangeName = "test_topic_exchange";
        String exchangeType = "topic";
        String queueName = "test_topic_queue";
        // String routingKey = "user.*";
        String routingKey = "user.#";
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        // 1 声明交换机, durable 是否持久化消息
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
        // 2 声明队列
        channel.queueDeclare(queueName, false, false, false, null);
        // 3 建立交换机和队列的绑定关系:
        channel.queueBind(queueName, exchangeName, routingKey);
        QueueingConsumer consumer = new QueueingConsumer(channel);
        // 参数：队列名称、是否自动ACK、Consumer
        channel.basicConsume(queueName, true, consumer);
        // 循环获取消息
        while (true) {
            // 获取消息，如果没有消息，这一步将会一直阻塞
            Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            log.info("消费端收到消息：{}", msg);
        }
    }

}

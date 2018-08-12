package com.hmily.rabbitmqapidemo.limit;

import com.hmily.rabbitmqapidemo.common.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 使用自定义消费者
 */
@Slf4j
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        //1 创建ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(RabbitMQConfig.RABBITMQ_HOST);
        connectionFactory.setPort(RabbitMQConfig.RABBITMQ_PORT);
        connectionFactory.setVirtualHost(RabbitMQConfig.RABBITMQ_DEFAULT_VIRTUAL_HOST);
        //2 获取C	onnection
        Connection connection = connectionFactory.newConnection();
        //3 通过Connection创建一个新的Channel
        Channel channel = connection.createChannel();
        String exchangeName = "test_qos_exchange";
        String queueName = "test_qos_queue";
        String routingKey = "qos.#";
        channel.exchangeDeclare(exchangeName, "topic", true, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);
        /**
         * prefetchSize：0
         prefetchCount：会告诉RabbitMQ不要同时给一个消费者推送多于N个消息，限速N个
            即一旦有N个消息还没有ack，则该consumer将block掉，直到有消息ack回来，你再发送N个过来
         global：true\false 是否将上面设置应用于channel级别，false是consumer级别
         prefetchSize 和global这两项，rabbitmq没有实现，暂且不研究
         */
        channel.basicQos(0, 1, false);

        //使用自定义消费者
        //1 限流方式  第一件事就是 autoAck设置为 false
        channel.basicConsume(queueName, false, new MyConsumer(channel));
        log.info("消费端启动成功");

    }
}

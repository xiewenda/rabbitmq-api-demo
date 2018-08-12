package com.hmily.rabbitmqapidemo.dlx;

import com.hmily.rabbitmqapidemo.common.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
        //2 获取Connection
        Connection connection = connectionFactory.newConnection();
        //3 通过Connection创建一个新的Channel
        Channel channel = connection.createChannel();

        // 这就是一个普通的交换机 和 队列 以及路由
        String exchangeName = "test_dlx_exchange";
        String routingKey = "dlx.#";
        String queueName = "test_dlx_queue";
        channel.exchangeDeclare(exchangeName, "topic", true, false, null);

        Map<String, Object> agruments = new HashMap<String, Object>();
        agruments.put("x-dead-letter-exchange", "dlx.exchange");
        //这个agruments属性，要设置到声明队列上
        channel.queueDeclare(queueName, true, false, false, agruments);
        channel.queueBind(queueName, exchangeName, routingKey);

        //要进行死信队列的声明: dlx.exchange/queue都是由你自己命名的，只不过为了这里只是为了简洁明了而已
        channel.exchangeDeclare("dlx.exchange", "topic", true, false, null);
        channel.queueDeclare("dlx.queue", true, false, false, null);
        channel.queueBind("dlx.queue", "dlx.exchange", "#");

        channel.basicConsume(queueName, true, new MyConsumer(channel));
        log.info("消费端启动成功");
    }
}

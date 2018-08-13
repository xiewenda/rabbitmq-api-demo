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
public class ProducerTopicExchange {
	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(RabbitMQConfig.RABBITMQ_HOST);
		connectionFactory.setPort(RabbitMQConfig.RABBITMQ_PORT);
		connectionFactory.setVirtualHost(RabbitMQConfig.RABBITMQ_DEFAULT_VIRTUAL_HOST);

		// 2 创建Connection
		Connection connection = connectionFactory.newConnection();
		// 3 创建Channel
		Channel channel = connection.createChannel();
		// 4 声明
		String exchangeName = "test_topic_exchange";
		String routingKey1 = "user.save";
		String routingKey2 = "user.update";
		String routingKey3 = "user.delete.abc";
		
		String msg = "Hello World RabbitMQ  Topic Exchange Message ...";
		log.info("生产端， {} ：{}", routingKey1, msg);
		channel.basicPublish(exchangeName, routingKey1 , null , (routingKey1 + msg).getBytes());
		log.info("生产端， {} ：{}", routingKey2, msg);
		channel.basicPublish(exchangeName, routingKey2 , null , (routingKey2 + msg).getBytes());
		log.info("生产端， {} ：{}", routingKey3, msg);
		channel.basicPublish(exchangeName, routingKey3 , null , (routingKey3 + msg).getBytes());
		channel.close();  
        connection.close(); 
	}

}

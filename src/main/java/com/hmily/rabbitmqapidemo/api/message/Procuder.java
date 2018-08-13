package com.hmily.rabbitmqapidemo.api.message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hmily.rabbitmqapidemo.common.RabbitMQConfig;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Procuder {
	private final static Logger log = LoggerFactory.getLogger(Procuder.class);

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(RabbitMQConfig.RABBITMQ_HOST);
		connectionFactory.setPort(RabbitMQConfig.RABBITMQ_PORT);
		connectionFactory.setVirtualHost(RabbitMQConfig.RABBITMQ_DEFAULT_VIRTUAL_HOST);

		// 2 通过连接工厂创建连接
		Connection connection = connectionFactory.newConnection();

		// 3 通过connection创建一个Channel
		Channel channel = connection.createChannel();

		Map<String, Object> headers = new HashMap<>();
		headers.put("myHeaders1", "111");
		headers.put("myHeaders2", "222");

		AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().deliveryMode(2).contentEncoding("UTF-8")
				.expiration("10000").headers(headers).build();

		// 4 通过Channel发送数据
		for (int i = 0; i < 5; i++) {
			String msg = "Hello RabbitMQ!";
			// 1 exchange 2 routingKey
			log.info("生产端，test001： {}", msg);
			channel.basicPublish("", "test001", properties, msg.getBytes());
		}

		// 5 记得要关闭相关的连接
		channel.close();
		connection.close();
	}

}

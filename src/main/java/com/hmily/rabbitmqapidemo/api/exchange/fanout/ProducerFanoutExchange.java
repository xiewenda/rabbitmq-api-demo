package com.hmily.rabbitmqapidemo.api.exchange.fanout;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hmily.rabbitmqapidemo.common.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ProducerFanoutExchange {

	private final static Logger log = LoggerFactory.getLogger(ProducerFanoutExchange.class);

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
		String exchangeName = "test_fanout_exchange";
		// 5 发送
		for (int i = 0; i < 10; i++) {
			String msg = "Hello World RabbitMQ  FANOUT Exchange Message ...";
			log.info("生产端，routingKey{}: {}", i, msg);
			channel.basicPublish(exchangeName, "routingKey" + i, null, msg.getBytes());
		}
		channel.close();
		connection.close();
	}

}

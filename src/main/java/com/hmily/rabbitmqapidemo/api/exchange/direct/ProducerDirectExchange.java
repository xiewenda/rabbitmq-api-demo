package com.hmily.rabbitmqapidemo.api.exchange.direct;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hmily.rabbitmqapidemo.common.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ProducerDirectExchange {
	
	private final static Logger log = LoggerFactory.getLogger(ProducerDirectExchange.class);

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(RabbitMQConfig.RABBITMQ_HOST);
		connectionFactory.setPort(RabbitMQConfig.RABBITMQ_PORT);
		connectionFactory.setVirtualHost(RabbitMQConfig.RABBITMQ_DEFAULT_VIRTUAL_HOST);

		// 声明
		String exchangeName = "test_direct_exchange";
		String routingKey = "test.direct";
//		String routingKey = "test.direct.bbb";
//		String routingKey = "test.direct11.bbb";
		
		// 创建Connection
		Connection connection = connectionFactory.newConnection();
				//创建Channel
		Channel channel = connection.createChannel(); 
		
		String msg = "Hello World RabbitMQ  Direct Exchange test.direct Message  ... ";
		log.info("生产端发送了：{}", msg);
		channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());
	}

}

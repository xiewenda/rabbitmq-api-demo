package com.hmily.rabbitmqapidemo.returnlistener;

import com.hmily.rabbitmqapidemo.common.RabbitMQConfig;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
public class Procuder {
    public static void main(String[] args) throws IOException, TimeoutException {
        //1 创建ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(RabbitMQConfig.RABBITMQ_HOST);
        connectionFactory.setPort(RabbitMQConfig.RABBITMQ_PORT);
        connectionFactory.setVirtualHost(RabbitMQConfig.RABBITMQ_DEFAULT_VIRTUAL_HOST);
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String exchange = "test_return_exchange";
        String routingKey = "return.save";
        String routingKeyError = "abc.save";
        String routingKeyError2 = "abc.update";
        String msg = "Hello RabbitMQ Return Message";

        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange,
                                     String routingKey, AMQP.BasicProperties properties,
                                     byte[] body) throws IOException {
                log.info("---------handle  return----------");
                log.info("响应码replyCode: {}", replyCode);
                log.info("文本信息replyText: {}", replyText);
                log.info("exchange:  {}", exchange);
                log.info("routingKey:  {}", routingKey);
                log.info("properties:  {}", properties);
                log.info("body:  {}" ,new String(body));
            }
        });


        /**
         * 如果未true，则监听器会接收到路由不可达的消息，然后进行后续处理，
         * 如果未false，则broker端自动删除该消息。
         */
        log.info("生产端{}发送：{}", routingKey, msg + routingKey);
        channel.basicPublish(exchange, routingKey, true, null, (msg + routingKey).getBytes());
        log.info("生产端{}发送：{}", routingKeyError, msg + routingKeyError);
        channel.basicPublish(exchange, routingKeyError, true, null, (msg + routingKeyError).getBytes());
        log.info("生产端{}发送：{}", routingKeyError2, msg + routingKeyError2);
        channel.basicPublish(exchange, routingKeyError2, false, null, (msg + routingKeyError2).getBytes());
    }
}

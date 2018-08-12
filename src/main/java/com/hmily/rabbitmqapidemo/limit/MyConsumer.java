package com.hmily.rabbitmqapidemo.limit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 自定义消费者
 */
@Slf4j
public class MyConsumer extends DefaultConsumer {

    private Channel channel;
    public MyConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag,  //消费者标签
                               Envelope envelope,
                               AMQP.BasicProperties properties,
                               byte[] body) throws IOException {
        log.info("------MyConsumer-----consume message----------");
        log.info("consumerTag: " + consumerTag);
        log.info("envelope: " + envelope);
        log.info("properties: " + properties);
        log.info("body: " + new String(body));
        //一定要手动ACK回去
        channel.basicAck(envelope.getDeliveryTag(), false);
    }
}

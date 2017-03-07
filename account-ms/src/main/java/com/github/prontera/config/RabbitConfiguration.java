package com.github.prontera.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zhao Junjian
 */
@Configuration
public class RabbitConfiguration {

    public static final String DEFAULT_FANOUT_EXCHANGE = "prontera.fanout";

    public static final String DEFAULT_DIRECT_EXCHANGE = "prontera.direct";
    public static final String TRADE_QUEUE = "funds";
    public static final String TRADE_DEAD_QUEUE = TRADE_QUEUE + "-dead-letter";
    public static final String TRADE_ROUTE_KEY = "trading";
    public static final String TRADE_DEAD_ROUTE_KEY = TRADE_ROUTE_KEY + "-dead";

    public static final String DEFAULT_TOPIC_EXCHANGE = "prontera.topic";
    public static final String TOPIC_ROUTE_KEY = "NYSE.TECH.MSFT";

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(DEFAULT_TOPIC_EXCHANGE, true, true);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DEFAULT_DIRECT_EXCHANGE, true, true);
    }

    @Bean
    public Queue tradeQueue() {
        //final ImmutableMap<String, Object> args = ImmutableMap.of("x-dead-letter-exchange", DEFAULT_DIRECT_EXCHANGE,
        //        "x-dead-letter-routing-key", TRADE_DEAD_ROUTE_KEY, "x-description", "用于account-ms与order-ms之间的资金交易");
        //return new Queue(TRADE_QUEUE, true, false, true, args);
        return new Queue(TRADE_QUEUE, true, false, true);
    }

    @Bean
    public Binding tradeBinding() {
        return BindingBuilder.bind(tradeQueue()).to(directExchange()).with(TRADE_ROUTE_KEY);
    }

    //@Bean
    public Queue deadLetterQueue() {
        return new Queue(TRADE_DEAD_QUEUE, true, false, true);
    }

    //@Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(directExchange()).with(TRADE_DEAD_ROUTE_KEY);
    }

}

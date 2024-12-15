package microservices.exam.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

@Slf4j
@Configuration
public class AMQPConfiguration {

    @Bean
    public FanoutExchange bookExchange(@Value("${amqp.exchange.name}") String exchangeName) {
        log.info("Creating FanoutExchange with name: {}", exchangeName);
        return ExchangeBuilder.fanoutExchange(exchangeName).durable(true).build();
    }

    @Bean
    public Queue bookQueue(@Value("${amqp.queue.book}") String bookQueueName) {
        log.info("Creating Queue with name: {}", bookQueueName);
        return QueueBuilder.durable(bookQueueName).build();
    }

    @Bean
    public Binding bookBinding(Queue bookQueue, FanoutExchange bookExchange) {
        log.info("Binding Queue '{}' to Exchange '{}'", bookQueue.getName(), bookExchange.getName());
        return BindingBuilder.bind(bookQueue).to(bookExchange);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        log.info("Configuring Jackson2JsonMessageConverter");
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        log.info("Creating RabbitTemplate");
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public MessageHandlerMethodFactory messageHandlerMethodFactory() {
        log.info("Configuring MessageHandlerMethodFactory");
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        MappingJackson2MessageConverter jsonConverter = new MappingJackson2MessageConverter();
        factory.setMessageConverter(jsonConverter);
        return factory;
    }

    @Bean
    public RabbitListenerConfigurer rabbitListenerConfigurer(MessageHandlerMethodFactory messageHandlerMethodFactory) {
        log.info("Configuring RabbitListenerConfigurer");
        return (c) -> c.setMessageHandlerMethodFactory(messageHandlerMethodFactory);
    }
}

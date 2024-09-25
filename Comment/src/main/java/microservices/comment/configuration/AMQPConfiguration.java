package microservices.comment.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

@Configuration
public class AMQPConfiguration {

    @Bean
    public TopicExchange bookTopicExchange(
            @Value("${amqp.exchange.name}") final String exchangeName
    ) {
        return ExchangeBuilder
                .topicExchange(exchangeName)
                .durable(true)
                .build();
    }

    @Bean
    public Queue commentQueue(
            @Value("${amqp.queue.name}") final String queueName
    ){
        return QueueBuilder
                .durable(queueName)
                .build();
    }

    @Bean
    public Binding bookCommentCreatedBinding(
            final Queue commentQueue,
            final TopicExchange bookTopicExchange
    ){
        return BindingBuilder
                .bind(commentQueue)
                .to(bookTopicExchange)
                .with("book.comment.created");
    }

    @Bean
    public Binding bookCommentUpdatedBinding(
            final Queue commentQueue,
            final TopicExchange bookTopicExchange
    ){
        return BindingBuilder
                .bind(commentQueue)
                .to(bookTopicExchange)
                .with("book.comment.updated");
    }

    @Bean
    public Binding bookCommentDeletedBinding(
            final Queue commentQueue,
            final TopicExchange bookTopicExchange
    ){
        return BindingBuilder
                .bind(commentQueue)
                .to(bookTopicExchange)
                .with("book.comment.deleted");
    }


    @Bean
    public MessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        final MappingJackson2MessageConverter jsonConverter =
                new MappingJackson2MessageConverter();
        jsonConverter.getObjectMapper().registerModule(
                new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));

        factory.setMessageConverter(jsonConverter);
        return factory;
    }

    @Bean
    public RabbitListenerConfigurer rabbitListenerConfigurer(
            final MessageHandlerMethodFactory messageHandlerMethodFactory) {
        return (c) -> c.setMessageHandlerMethodFactory(messageHandlerMethodFactory);
    }
}

package microservices.comment.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.amqp.core.*;
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
            @Value("${amqp.exchange.book}") final String exchangeName
    ){
        return ExchangeBuilder
                .topicExchange(exchangeName)
                .durable(true)
                .build();
    }

    @Bean
    public Queue commentQueue(
            @Value("${amqp.queue.comment}") final String queueName
    ){
        return QueueBuilder
                .durable(queueName)
                .build();
    }

    @Bean
    public Binding bookCommentBinding(
            final Queue commentQueue,
            final TopicExchange bookTopicExchange
    ) {
        return BindingBuilder
                .bind(commentQueue)
                .to(bookTopicExchange)
                .with("book.comment.#");
    }

    @Bean
    public MessageHandlerMethodFactory messageHandlerMethodFactory(){
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();

        final MappingJackson2MessageConverter jsonConverter =
                new MappingJackson2MessageConverter();
        jsonConverter.getObjectMapper().registerModule(
                new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));

        factory.setMessageConverter(jsonConverter);
        return factory;

    }
}




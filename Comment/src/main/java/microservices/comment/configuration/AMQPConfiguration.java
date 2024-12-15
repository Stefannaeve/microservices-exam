package microservices.comment.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AMQPConfiguration {

    @Bean
    public FanoutExchange commentExchange(@Value("${amqp.exchange.comment}") String exchangeName) {
        return ExchangeBuilder.fanoutExchange(exchangeName).durable(true).build();
    }

    @Bean
    public Queue commentQueue(@Value("${amqp.queue.comment}") String queueName) {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Binding commentBinding(Queue commentQueue, FanoutExchange commentExchange) {
        return BindingBuilder.bind(commentQueue).to(commentExchange);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jackson2JsonMessageConverter());
        return template;
    }
}

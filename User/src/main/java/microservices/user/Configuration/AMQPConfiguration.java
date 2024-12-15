package microservices.user.Configuration;

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
    public FanoutExchange userExchange(@Value("${amqp.exchange.name}") String exchangeName) {
        log.info("Creating FanoutExchange with name: {}", exchangeName);
        return ExchangeBuilder.fanoutExchange(exchangeName).durable(true).build();
    }

    @Bean
    public Queue userQueue(@Value("${amqp.queue.user}") String userQueueName) {
        log.info("Creating Queue with name: {}", userQueueName);
        return QueueBuilder.durable(userQueueName).build();
    }

    @Bean
    public Binding userBinding(Queue userQueue, FanoutExchange userExchange) {
        log.info("Binding Queue '{}' to Exchange '{}'", userQueue.getName(), userExchange.getName());
        return BindingBuilder.bind(userQueue).to(userExchange);
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
}

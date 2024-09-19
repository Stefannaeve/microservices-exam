package microservices.exam.eventDriven;
import lombok.extern.slf4j.Slf4j;


import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BookEventPublisher {

    private final AmqpTemplate amqpTemplate;
    private final String exchangeName;

    public BookEventPublisher(
            final AmqpTemplate amqpTemplate,
            @Value("${amqp.exchange.name}") final String exchangeName
    ){
        this.amqpTemplate = amqpTemplate;
        this.exchangeName = exchangeName;
    }

    public void publishBookEventString(
            BookEvent bookEvent
    ){
        // build the message/event
        String event = buildEventString(bookEvent);
        // decide on routing
        String routingKey = "book." + (bookEvent.getId() + "comment.created");
        // send the thing
        amqpTemplate.convertAndSend(exchangeName, routingKey, event);
    }

    public String buildEventString(BookEvent bookEvent){
        StringBuffer eventBuffer = new StringBuffer();

        eventBuffer.append("{")
                .append("\"id\":" + bookEvent.getId() + ",")
                .append("\"page\":" + bookEvent.getPage() + ",")
                .append("\"text\":" + bookEvent.getText() + ",")
                .append("}");
        return eventBuffer.toString();
    }
}

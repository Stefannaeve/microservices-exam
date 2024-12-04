
package microservices.exam.eventDriven;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BookEventListener {

    @RabbitListener(queues = "${amqp.queue.book}")
    public void handleBookCreatedEvent(BookEvent bookEvent) {
        log.info("Received Book Event: {}", bookEvent);
        // Process the book event
    }


}

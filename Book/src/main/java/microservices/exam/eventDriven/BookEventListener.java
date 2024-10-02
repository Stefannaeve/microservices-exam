package microservices.exam.eventDriven;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BookEventListener {

    @RabbitListener(queues = "${amqp.queue.name}")
    public void handleBookCreatedEvent(BookEvent bookEvent) {
        try {
            log.info("Processing message... Slowing down for demo purposes.");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread was interrupted", e);
        }

        log.info("Received Book Event: Title - {}, Author - {}, Pages - {}, PublishDate - {}, BookContent - {}",
                bookEvent.getTitle(), bookEvent.getAuthor(), bookEvent.getPages(), bookEvent.getPublishDate(), bookEvent.getBookContent());

    }
}

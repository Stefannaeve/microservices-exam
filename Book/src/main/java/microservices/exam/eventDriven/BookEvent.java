package microservices.exam.eventDriven;

import lombok.Value;

@Value
public class BookEvent {

    Long id;
    int page;
    String text;
}

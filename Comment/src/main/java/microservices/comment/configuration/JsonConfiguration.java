package microservices.comment.configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class JsonConfiguration {

    @Bean
    public Module hibernateModule(){
        return new Hibernate5Module();
    }
}

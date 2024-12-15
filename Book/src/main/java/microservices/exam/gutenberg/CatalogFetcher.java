package microservices.exam.gutenberg;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.exam.models.Book;
import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpResponse;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@NoArgsConstructor
@Service
public class CatalogFetcher {



    public static List<Book> populateDatabaseFromGutenberg(int maxBookCount){
        RestTemplate restTemplate = new RestTemplate();

        List<Book> books;

        try {
            books = restTemplate.execute(
                "https://www.gutenberg.org/cache/epub/feeds/pg_catalog.csv",
                HttpMethod.GET,
                null,
                clientHttpResponse -> {
                    InputStreamReader reader = new InputStreamReader(clientHttpResponse.getBody());

                    CsvToBean<Book> csvToBean = new CsvToBeanBuilder<Book>(reader)
                            .withType(Book.class)
                            .withSeparator(',')
                            .build();

                    List<Book> parsedBooks = new ArrayList<>();

                    Iterator<Book> iterator = csvToBean.iterator();
                    int i = 0;

                    while (iterator.hasNext() == true && i < maxBookCount) {
                        parsedBooks.add(iterator.next());
                        iterator.next();
                        i++;
                    }

                    return parsedBooks;
                }
            );

            return books;

        } catch (RestClientException clientException) {
            log.error("Encountered error while connecting to external service");
            log.error(clientException.getMessage());
            clientException.printStackTrace();
        } catch (ClassCastException | NullPointerException | NoSuchElementException parsingException) {
            log.error("Encountered error while parsing response from external service");
            log.error(parsingException.getMessage());
            parsingException.printStackTrace();
        } catch (Exception exception) {
            log.error("Unexpected error occurred");
            log.error(exception.getMessage());
            exception.printStackTrace();
        }
        //TODO returns for errors
        return null;

    }

    public static String fetchBookContentFromGutenberg(Long bookId) {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setRedirectStrategy(new DefaultRedirectStrategy())
                .build();

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));


        String url = String.format("https://gutenberg.org/ebooks/%d.txt.utf-8", bookId.intValue());

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            //Print for testing
            log.info("Response Code: {}", response.getStatusCode());
            log.info("Response Headers: {}", response.getHeaders());
            log.info("Response Body: {}", response.getBody());

            return response.getBody();
        } catch (RestClientException clientException) {
            log.error("Encountered error while connecting to external service");
            log.error(clientException.getMessage());
            clientException.printStackTrace();
        } catch (Exception exception) {
            log.error("Unexpected error occurred");
            log.error(exception.getMessage());
            exception.printStackTrace();
        }
        //TODO returns for errors
        return null;
    }
}

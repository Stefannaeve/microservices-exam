package microservices.exam.apiResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseEntityInitializer<T> {
    public static <T> ResponseEntity<T> NewResponseEntity(HttpStatus status, boolean success, String errorMessage, String exceptionDump, HttpStatus errorStatus, T value) {
        HttpHeaders headers = new HttpHeaders();
        if (success) {
            headers.add("Success", "true");
        } else {
            headers.add("Success", "false");
            headers.add("ErrorMessage", errorMessage);
            headers.add("ExceptionDump", exceptionDump);
            headers.add("ErrorStatus", String.valueOf(errorStatus.value()));
        }
        return new ResponseEntity<T>(value, headers, status);
    }

    public static <T> ResponseEntity<T> NewResponseEntity(HttpStatus status, boolean success, String errorMessage, HttpStatus errorStatus,  T value) {
        HttpHeaders headers = new HttpHeaders();
        if (success) {
            headers.add("Success", "true");
        } else {
            headers.add("Success", "false");
            headers.add("ErrorMessage", errorMessage);
            headers.add("ErrorStatus", String.valueOf(errorStatus.value()));
        }
        return new ResponseEntity<T>(value, headers, status);
    }



    public static <T> ResponseEntity<T> NewResponseEntity(HttpStatus status, T value) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Success", "true");

        return new ResponseEntity<T>(value, headers, status);
    }

    public static String extractHeader(ResponseEntity entity, String headerName){
        HttpHeaders headers = entity.getHeaders();

        List<String> customHeaderValues = headers.get("X-Custom-Header");

        if (customHeaderValues != null && !customHeaderValues.isEmpty()) {
            String customHeaderValue = customHeaderValues.get(0);
            return customHeaderValue;
        }
        return "";
    }
}
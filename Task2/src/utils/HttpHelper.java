package utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import parameters.ErrorMessages;
import usersExceptions.HttpsExceptions;

import java.util.Map;

public class HttpHelper {

    private static RestTemplate getInstance() {

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate;
    }

    public static String executeRequest(String url, Map<String, String> headers, HttpMethod method) throws HttpsExceptions {
        ResponseEntity<String> responseEntity;
        if (method != null) {
            if (headers != null) {
                HttpEntity entity = prepareHeadersToEntity(headers);
                responseEntity = getInstance()
                        .exchange(url, method, entity, String.class);
            } else {
                responseEntity = getInstance()
                        .getForEntity(url, String.class);
            }
            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new HttpsExceptions(String.format(ErrorMessages.BAD_STATUS_RESPONSE, responseEntity.getStatusCode().value()));
            }
            return responseEntity.getBody();
        } else {
            throw new HttpsExceptions(String.format(ErrorMessages.UNKNOWN_TYPE_REQUEST, method.name()));
        }
    }

    private static HttpEntity prepareHeadersToEntity(Map<String, String> headers) {
        HttpHeaders headersForEntity = new HttpHeaders();
        for (Map.Entry<String, String> pair : headers.entrySet()) {
            headersForEntity.set(pair.getKey(), pair.getValue());
        }
        return new HttpEntity(headersForEntity);
    }
}

package utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.Map;

public class HttpHelper {
    public static HttpResponse executeGetRequest(String url, Map<String, String> headers) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);
        for (Map.Entry<String, String> pair : headers.entrySet()) {
            httpGet.addHeader(pair.getKey(), pair.getValue());
        }
        HttpResponse response = client.execute(httpGet);
        return response;
    }
}

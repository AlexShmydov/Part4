package utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpHelper {
    public static StringBuffer executeGetRequest(String url, Map<String, String> headers) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);
        for (Map.Entry<String, String> pair : headers.entrySet()) {
            httpGet.addHeader(pair.getKey(), pair.getValue());
        }
        HttpResponse response = client.execute(httpGet);
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result;
    }

    public static StringBuffer executeGetRequest(String url) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = client.execute(httpGet);
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result;
    }

    public static StringBuffer executePutRequest(String url, Map<String, String> headers) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPut httpPut = new HttpPut(url);
        HttpResponse response = client.execute(httpPut);
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result;
    }

    public static String getJSONParameterByKey(String jsonBody, String key) {
        JSONObject obj = new JSONObject(jsonBody);
        return obj.getString(key);
    }

    public static List<String> getJSONObjectsFromArray(String jsonBody) {
        List<String> jsonArrays = new ArrayList<>();
        JSONArray array = new JSONArray(jsonBody);
        for (int i = 0; i < array.length(); i++) {
            jsonArrays.add(array.get(i).toString());
        }
        return jsonArrays;
    }
}

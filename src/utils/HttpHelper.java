package utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import parameters.ErrorMessages;
import usersExceptions.HttpsExceptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpHelper {
    private static final int GOOD_HTTP_CODE = 200;

    public static String executeRequest(String methodName, String url, Map<String, String> headers) throws IOException, HttpsExceptions {
        HttpClient client = HttpClientBuilder.create().build();
        HttpRequestBase method;
        switch (methodName) {
            case HttpGet.METHOD_NAME:
                method = new HttpGet(url);
                break;
            case HttpPut.METHOD_NAME:
                method = new HttpPut(url);
                break;
            default:
                method = null;
                break;
        }
        if (method != null) {
            if (headers != null) {
                for (Map.Entry<String, String> pair : headers.entrySet()) {
                    method.addHeader(pair.getKey(), pair.getValue());
                }
            }
            HttpResponse response = client.execute(method);
            if (response.getStatusLine().getStatusCode() - GOOD_HTTP_CODE >= 0 && response.getStatusLine().getStatusCode() - GOOD_HTTP_CODE < 99) {
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));
                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            } else {
                throw new HttpsExceptions(String.format(ErrorMessages.BAD_STATUS_RESPONSE, response.getStatusLine().getStatusCode()));
            }
        } else {
            throw new HttpsExceptions(String.format(ErrorMessages.UNKNOWN_TYPE_REQUEST, methodName));
        }
    }

    public static String getJSONParameterByKey(String jsonBody, String key) {
        JSONObject obj = new JSONObject(jsonBody);
        return obj.get(key).toString();
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

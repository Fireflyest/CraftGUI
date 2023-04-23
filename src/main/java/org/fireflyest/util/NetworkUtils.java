package org.fireflyest.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class NetworkUtils {

    private static final Gson gson = new GsonBuilder().create();

    private NetworkUtils() {
    }

    public static <T> T doGet(String uri, Class<T> aClass) {
        T t = null;
        try {
            String resultString = getRequest(uri);
            t = gson.fromJson(resultString, aClass);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return t;
    }

    private static String getRequest(String uri) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static <T> T doPost(String uri, Map<String, String> values, Class<T> aClass) {
        return doPost(uri, gson.toJson(values), aClass);
    }

    public static <T> T doPost(String uri, List<String> values, Class<T> aClass) {
        return doPost(uri, gson.toJson(values), aClass);
    }

    public static <T> T doPost(String uri, String data, Class<T> aClass) {
        T t = null;
        try {
            String resultString = postRequest(uri, data);
            t = gson.fromJson(resultString, aClass);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return t;
    }

    private static String postRequest(String uri, String values) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .POST(HttpRequest.BodyPublishers.ofString(values))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    
}

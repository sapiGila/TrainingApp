package com.training.app.util.restapi;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

/**
 * Created by user on 6/2/2017.
 */

public class OkHttpClientFactory {

    private static final long TIMEOUT_CONNECTION_SECONDS = 60;
    private static final int MAX_REST_API_IDLE_CONNECTION = 1;
    private static final long KEEP_ALIVE_IDLE_DURATION_MS = 60000;

    private static OkHttpClientFactory okHttpClientFactory;
    private OkHttpClient.Builder okHttpClientForRestAdapter;

    private OkHttpClient.Builder createHttpClientForRestAdapter() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(TIMEOUT_CONNECTION_SECONDS, TimeUnit.SECONDS);
        httpClient.readTimeout(TIMEOUT_CONNECTION_SECONDS, TimeUnit.SECONDS);
        httpClient.connectionPool(new ConnectionPool(MAX_REST_API_IDLE_CONNECTION, KEEP_ALIVE_IDLE_DURATION_MS,
                TimeUnit.SECONDS));
        return httpClient;
    }

    public static OkHttpClientFactory getInstance() {
        if (okHttpClientFactory == null) {
            okHttpClientFactory = new OkHttpClientFactory();
        }
        return okHttpClientFactory;
    }

    private OkHttpClientFactory() {
        okHttpClientForRestAdapter = createHttpClientForRestAdapter();
    }

    public OkHttpClient.Builder getOkHttpClientForRestAdapter() {
        return okHttpClientForRestAdapter;
    }
}

package com.training.app.util.restapi;

import com.training.app.util.VariableUtil;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by user on 6/2/2017.
 */

public class BaseRestApiAdapter {

    protected static Retrofit restAdapter;
    protected static boolean isDebugMode = VariableUtil.IS_DEBUG_MODE;

    protected static Retrofit getRestAdapter(String serverURL) {
        OkHttpClientFactory okHttpClientFactory = OkHttpClientFactory.getInstance();
        HttpLoggingInterceptor logging = getLoggingLevel(isDebugMode);
        okHttpClientFactory.getOkHttpClientForRestAdapter().addInterceptor(logging);
        return new Retrofit.Builder()
                .baseUrl(serverURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClientFactory.getOkHttpClientForRestAdapter().build())
                .build();
    }

    private static HttpLoggingInterceptor getLoggingLevel(boolean isDebugMode) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (isDebugMode) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        return logging;
    }


}

package com.jwoos.android.sellbook.base.retrofit;



import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by renegens on 16/02/16.
 */
public class ServiceGenerator {

    private static final String API_BASE_URL = "http://fack14.cafe24.com/SellBook";
    private static final String API_BASE_URL2 = "http://www.aladin.co.kr/ttb/api";
    private static final long tiempoMaximoRespuestaSegundos = 600;
    private static final long tiempoMaximoLecturaSegundos = 1000;
    private static OkHttpClient clienteOkHttp = new OkHttpClient();


    private static RestAdapter.Builder builder = new RestAdapter.Builder()
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setEndpoint(API_BASE_URL)
            .setClient(new OkClient(clienteOkHttp));


    private static <S> S createService(Class<S> serviceClass) {
        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {

                request.addHeader("Authorization", "auth-value");
                request.addHeader("Accept", "application/json");
            }
        });
        clienteOkHttp.interceptors().add(new AddCookiesInterceptor());
        clienteOkHttp.interceptors().add(new ReceivedCookiesInterceptor());
        clienteOkHttp.setConnectTimeout(tiempoMaximoRespuestaSegundos, TimeUnit.SECONDS);
        clienteOkHttp.setReadTimeout(tiempoMaximoLecturaSegundos, TimeUnit.SECONDS);
        RestAdapter adapter = builder.build();
        return adapter.create(serviceClass);
    }

    private static final Retrofit_api service = ServiceGenerator.createService(Retrofit_api.class);

    public static Retrofit_api getService() {
        return service;
    }


}
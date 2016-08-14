package com.jwoos.android.sellbook.base.retrofit;



import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.SimpleXMLConverter;

/**
 * Created by renegens on 16/02/16.
 */
public class ServiceGenerator2 {

    private static final String API_BASE_URL = "https://openapi.naver.com/v1/search/book_adv.xml";
    private static final long tiempoMaximoRespuestaSegundos = 600;
    private static final long tiempoMaximoLecturaSegundos = 1000;
    private static OkHttpClient clienteOkHttp = new OkHttpClient();


    private static RestAdapter.Builder builder = new RestAdapter.Builder()
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setEndpoint(API_BASE_URL)
            .setConverter(new SimpleXMLConverter())
            .setClient(new OkClient(clienteOkHttp));


    private static <S> S createService(Class<S> serviceClass) {
        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {

                request.addHeader("Authorization", "auth-value");
                request.addHeader("Accept", "application/json");
                request.addHeader("X-Naver-Client-Id","S3Uth6DNCQTj6psKgg8I");
                request.addHeader("X-Naver-Client-Secret","GyFh_DlXGW");
            }
        });
        clienteOkHttp.interceptors().add(new AddCookiesInterceptor());
        clienteOkHttp.interceptors().add(new ReceivedCookiesInterceptor());
        clienteOkHttp.setConnectTimeout(tiempoMaximoRespuestaSegundos, TimeUnit.SECONDS);
        clienteOkHttp.setReadTimeout(tiempoMaximoLecturaSegundos, TimeUnit.SECONDS);
        RestAdapter adapter = builder.build();
        return adapter.create(serviceClass);
    }

    private static final Retrofit_api service = ServiceGenerator2.createService(Retrofit_api.class);

    public static Retrofit_api getService() {
        return service;
    }


}
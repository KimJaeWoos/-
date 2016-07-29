package com.jwoos.android.sellbook.base.retrofit;

import android.content.Context;

import com.jwoos.android.sellbook.base.db.Preference;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashSet;

/**
 * Created by Jwoo on 2016-06-24.
 */
public class ReceivedCookiesInterceptor implements Interceptor {

    private Context context;
    public static final String PREF_COOKIES = "PREF_COOKIES";


    public ReceivedCookiesInterceptor() {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);

            }
            // Preference에 cookies를 넣어주는 작업을 수행
            Preference pref = new Preference(context);
            pref.setCookie(cookies);
            //HashSet<String> mySet = new HashSet<String>(ss.getStringSet("cookie",new HashSet<String>()));

        }

        return originalResponse;
    }
}
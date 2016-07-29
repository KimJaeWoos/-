package com.jwoos.android.sellbook.base.retrofit;

import android.content.Context;
import android.util.Log;

import com.jwoos.android.sellbook.base.db.Preference;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jwoo on 2016-06-24.
 */
public class AddCookiesInterceptor implements Interceptor {

    private Context context;
    public static final String PREF_COOKIES = "PREF_COOKIES";

    public AddCookiesInterceptor() {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        // Preference에서 cookies를 가져오는 작업을 수행
        Preference pref = new Preference(context);
        Set<String> preferences =  pref.getCookie();
        //Set<String> preferences =  SharedPreferenceBase.getSharedPreference("cookie", new HashSet<String>());

        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
        }

        // Web,Android,iOS 구분을 위해 User-Agent세팅
        builder.removeHeader("User-Agent").addHeader("User-Agent", "Android");


        return chain.proceed(builder.build());
    }
}

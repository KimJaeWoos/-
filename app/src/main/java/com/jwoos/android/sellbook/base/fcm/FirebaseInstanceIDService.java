package com.jwoos.android.sellbook.base.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.jwoos.android.sellbook.base.retrofit.ServiceGenerator;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by filipp on 5/23/2016.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();
        registerToken(token);
    }

    private void registerToken(String token) {

        ServiceGenerator.getService().registerToken(token, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}

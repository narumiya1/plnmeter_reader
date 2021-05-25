package com.example.myapplicationpln.cookies;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    protected String jwt;
    @Override
    public Response intercept(Chain chain) throws IOException {

        //rewrite the request to add bearer token
        Request newRequest=chain.request().newBuilder()
                .header("Authorization","Bearer "+jwt)
                .build();

        return chain.proceed(newRequest);
    }
    public TokenInterceptor(String jwt) {
        super();
        this.jwt = jwt;
    }
}

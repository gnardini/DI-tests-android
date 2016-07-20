package com.gnardini.testapplication.network;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitServices {

    private static Retrofit sRetrofit;
    private static Map<Class, Object> sServices;

    public static void init() {
        sServices = new HashMap<>();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        sRetrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.0.169:3000")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static <T> T getService(Class<T> clazz) {
        T service = (T) sServices.get(clazz);
        if (service != null) return service;
        service = sRetrofit.create(clazz);
        sServices.put(clazz, service);
        return service;
    }

    public static UsersService users() {
        return getService(UsersService.class);
    }

}

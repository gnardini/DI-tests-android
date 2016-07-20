package com.gnardini.testapplication.network;

import com.gnardini.testapplication.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UsersService {

    @POST("/users/sign_in")
    Call<User> logIn(@Body UserRequest user);

}

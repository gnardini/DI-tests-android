package com.gnardini.testapplication.repository;

import com.gnardini.testapplication.model.User;
import com.gnardini.testapplication.network.UserRequest;
import com.gnardini.testapplication.network.UsersService;
import com.gnardini.testapplication.wolox.WoloxCallback;

public class UsersRepository {

    private final UsersService usersService;

    public UsersRepository(UsersService usersService) {
        this.usersService = usersService;
    }

    public void login(UserRequest userRequest, final RepoCallback<User> callback) {
        usersService.logIn(userRequest)
                .enqueue(new WoloxCallback<User>(callback.getPresenter()) {
                    @Override
                    public void onSuccess(User user) {
                        callback.onSuccess(user);
                    }

                    @Override
                    public void onCallFailed(String error, int code) {
                        callback.onError(error, code);
                    }
                });
    }

}

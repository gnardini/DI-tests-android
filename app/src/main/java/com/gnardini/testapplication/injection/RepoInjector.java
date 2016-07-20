package com.gnardini.testapplication.injection;

import com.gnardini.testapplication.network.RetrofitServices;
import com.gnardini.testapplication.network.UsersService;
import com.gnardini.testapplication.repository.UsersRepository;

public class RepoInjector {

    private UsersService usersService;
    public UsersService getUsersService() {
        if (usersService == null) {
            usersService = RetrofitServices.users();
        }
        return usersService;
    }

    private UsersRepository usersRepository;
    public UsersRepository getUsersRepository() {
        if (usersRepository == null) {
            usersRepository = new UsersRepository(getUsersService());
        }
        return usersRepository;
    }

}

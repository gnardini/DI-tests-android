package com.gnardini.testapplication.injection;

import com.gnardini.testapplication.login.LoginPresenter;
import com.gnardini.testapplication.login.LoginView;

public class PresenterInjector {

    private final CommonInjector commonInjector;
    private final RepoInjector repoInjector;

    public PresenterInjector(CommonInjector commonInjector, RepoInjector repoInjector) {
        this.commonInjector = commonInjector;
        this.repoInjector = repoInjector;
    }

    public LoginPresenter createLoginPresenter(LoginView loginView) {
        return new LoginPresenter(
                loginView,
                commonInjector.getLocalStorage(),
                repoInjector.getUsersRepository());
    }

}

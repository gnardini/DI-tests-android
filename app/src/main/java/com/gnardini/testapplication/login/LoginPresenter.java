package com.gnardini.testapplication.login;

import com.gnardini.testapplication.Configuration;
import com.gnardini.testapplication.model.User;
import com.gnardini.testapplication.network.UserRequest;
import com.gnardini.testapplication.repository.LocalStorage;
import com.gnardini.testapplication.repository.RepoCallback;
import com.gnardini.testapplication.repository.UsersRepository;
import com.gnardini.testapplication.wolox.BasePresenter;

public class LoginPresenter extends BasePresenter<LoginView> {

    private final LocalStorage localStorage;
    private final UsersRepository usersRepository;

    private String email;
    private String password;

    public LoginPresenter(
            LoginView loginView,
            LocalStorage localStorage,
            UsersRepository usersRepository) {
        super(loginView);
        this.localStorage =  localStorage;
        this.usersRepository = usersRepository;

        credentialsUpdated();
    }

    public void updateEmail(String email) {
        this.email = email;
        credentialsUpdated();
    }

    public void updatePassword(String password) {
        this.password = password;
        credentialsUpdated();
    }

    public void loginPressed() {
        if (areCredentialsValid()) {
            doLogin();
        }
    }

    // Private methods.

    private void credentialsUpdated() {
        getView().setButtonEnabled(areCredentialsValid());
    }

    private boolean areCredentialsValid() {
        // TODO: Make better checks
        if (email == null || email.length() < 6) {
            return false;
        }
        if (password == null || password.length() < 6) {
            return false;
        }
        return true;
    }

    private void doLogin() {
        getView().showLoading();
        getView().setButtonEnabled(false);
        usersRepository.login(new UserRequest(email, password), new RepoCallback<User>(this) {
            @Override
            public void onSuccess(User user) {
                localStorage.storeInSharedPreferences(Configuration.KEY_EMAIL, user);
                getView().goToMainScreen();
                getView().hideLoading();
                getView().setButtonEnabled(true);
            }

            @Override
            public void onError(String error, int code) {
                getView().showError(error);
                getView().hideLoading();
                getView().setButtonEnabled(true);
            }
        });
    }

}

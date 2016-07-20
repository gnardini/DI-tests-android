package com.gnardini.testapplication.login;

public interface LoginView {

    void showLoading();

    void hideLoading();

    void setButtonEnabled(boolean enabled);

    void goToMainScreen();

    void showError(String error);

}

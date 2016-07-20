package com.gnardini.testapplication.login;

import android.widget.Button;

import com.gnardini.testapplication.R;
import com.gnardini.testapplication.wolox.WoloxActivity;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.OnTextChanged;

public class LoginActivity extends WoloxActivity<LoginPresenter> implements LoginView {

    @BindView(R.id.login_button_login) Button mLoginButton;

    @BindColor(R.color.prime_white_1) int mWhite;
    @BindColor(R.color.prime_grey_5) int mGrey;

    @Override
    protected int layout() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginPresenter createPresenter() {
        return getPresenterInjector().createLoginPresenter(this);
    }

    @Override
    protected void init() {
    }

    @OnTextChanged(R.id.login_field_email)
    void onEmailUpdated(CharSequence email) {
        getPresenter().updateEmail(email.toString());
    }

    @OnTextChanged(R.id.login_field_password)
    void onPasswordUpdated(CharSequence password) {
        getPresenter().updatePassword(password.toString());
    }

    @Override
    public void setButtonEnabled(boolean enabled) {
        mLoginButton.setEnabled(enabled);
        mLoginButton.setBackgroundColor(enabled ? mWhite : mGrey);
    }


    @Override
    public void goToMainScreen() {
        // TODO: Implement logged in app.
    }

    @Override
    public void showLoading() {
        // TODO
    }

    @Override
    public void hideLoading() {
        // TODO
    }

    @Override
    public void showError(String error) {
        // TODO
    }

}

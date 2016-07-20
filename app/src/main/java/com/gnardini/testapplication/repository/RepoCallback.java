package com.gnardini.testapplication.repository;

import com.gnardini.testapplication.wolox.BasePresenter;

public abstract class RepoCallback<T> {

    private BasePresenter basePresenter;

    public RepoCallback(BasePresenter basePresenter) {
        this.basePresenter = basePresenter;
    }

    public abstract void onSuccess(T value);

    public abstract void onError(String error, int code);

    public BasePresenter getPresenter() {
        return basePresenter;
    }

}

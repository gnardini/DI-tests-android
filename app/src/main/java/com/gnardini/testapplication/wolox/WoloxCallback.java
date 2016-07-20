package com.gnardini.testapplication.wolox;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class WoloxCallback<T> implements Callback<T> {

    private BasePresenter basePresenter;

    public WoloxCallback(BasePresenter basePresenter) {
        this.basePresenter = basePresenter;
    }

    @Override
    public final void onResponse(Call<T> call, Response<T> response) {
        if (!basePresenter.isViewAttached()) {
            return;
        }
        if (response.isSuccessful()) {
            onSuccess(response.body());
        } else {
            ResponseBody responseBody = response.errorBody();
            String errorString = "";
            if (responseBody != null) {
                try {
                    errorString = responseBody.string();
                } catch (IOException ioException) {
                }
            }
            onCallFailed(errorString, response.code());
        }
    }

    @Override
    public final void onFailure(Call<T> call, Throwable t) {
        if (!basePresenter.isViewAttached()) {
            return;
        }
        onCallFailed(null, 0);
    }

    /** Successful HTTP response */
    public abstract void onSuccess(T response);

    /** Successful HTTP response but has an error body */
    public abstract void onCallFailed(String error, int code);

}

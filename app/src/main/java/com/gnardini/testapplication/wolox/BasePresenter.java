package com.gnardini.testapplication.wolox;

import android.content.Context;
import android.support.annotation.Nullable;

/**
 * Abstract presenter that provides the view to the specific presenters.
 */
public class BasePresenter<T> {

    private T mViewInstance;
    private Context mContext;

    public BasePresenter(T viewInstance) {
        this.mViewInstance = viewInstance;
    }

    public BasePresenter(T viewInstance, Context context) {
        this.mViewInstance = viewInstance;
        this.mContext = context;
    }

    @Nullable
    protected T getView() {
        return mViewInstance;
    }

    protected Context getContext() {
        return mContext;
    }

    public boolean isViewAttached() {
        return mViewInstance != null;
    }

    public void detachView() {
        mViewInstance = null;
        mContext = null;
    }

}

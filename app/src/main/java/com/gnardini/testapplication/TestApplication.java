package com.gnardini.testapplication;

import android.app.Application;

import com.gnardini.testapplication.injection.CommonInjector;
import com.gnardini.testapplication.injection.CommonInjectorSupplier;
import com.gnardini.testapplication.injection.PresenterInjector;
import com.gnardini.testapplication.injection.PresenterInjectorSupplier;
import com.gnardini.testapplication.injection.RepoInjector;
import com.gnardini.testapplication.injection.RepoInjectorSupplier;
import com.gnardini.testapplication.network.RetrofitServices;

public class TestApplication
        extends Application
        implements
        CommonInjectorSupplier,
        PresenterInjectorSupplier,
        RepoInjectorSupplier {

    private CommonInjector commonInjector;
    private PresenterInjector presenterInjector;
    private RepoInjector repoInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitServices.init();
        commonInjector = new CommonInjector(this);
        repoInjector = new RepoInjector();
        presenterInjector = new PresenterInjector(commonInjector, repoInjector);
    }

    @Override
    public CommonInjector getCommonInjector() {
        return commonInjector;
    }

    @Override
    public PresenterInjector getPresenterInjector() {
        return presenterInjector;
    }

    @Override
    public RepoInjector getRepoInjector() {
        return repoInjector;
    }

}

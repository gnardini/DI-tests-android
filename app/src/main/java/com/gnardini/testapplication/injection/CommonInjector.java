package com.gnardini.testapplication.injection;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.gnardini.testapplication.repository.LocalStorage;

public class CommonInjector {

    private final Context context;

    public CommonInjector(Context context) {
        this.context = context;
    }

    private SharedPreferences sharedPreferences;
    public SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(
                    "TestApp_SharedPreferences",
                    Activity.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    private LocalStorage localStorage;
    public LocalStorage getLocalStorage() {
        if (localStorage == null) {
            localStorage = new LocalStorage(getSharedPreferences());
        }
        return localStorage;
    }

}

package com.gnardini.testapplication.wolox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.gnardini.testapplication.injection.CommonInjector;
import com.gnardini.testapplication.injection.CommonInjectorSupplier;
import com.gnardini.testapplication.injection.PresenterInjector;
import com.gnardini.testapplication.injection.PresenterInjectorSupplier;
import com.gnardini.testapplication.injection.RepoInjector;
import com.gnardini.testapplication.injection.RepoInjectorSupplier;

import butterknife.ButterKnife;

public abstract class WoloxActivity<T extends BasePresenter> extends FragmentActivity {

    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout());
        ButterKnife.bind(this);
        if (handleArguments(getIntent().getExtras())) {
            mPresenter = createPresenter();
            setUi();
            init();
            populate();
            setListeners();
        } else {
            finish();
        }
    }

    protected CommonInjector getCommonInjector() {
        return ((CommonInjectorSupplier) getApplication()).getCommonInjector();
    }

    protected RepoInjector getRepoInjector() {
        return ((RepoInjectorSupplier) getApplication()).getRepoInjector();
    }

    protected PresenterInjector getPresenterInjector() {
        return ((PresenterInjectorSupplier) getApplication()).getPresenterInjector();
    }

    /**
     * Returns the layout id for the inflater so the view can be populated
     */
    protected abstract int layout();

    /**
     * Reads arguments sent as a Bundle and saves them as appropriate.
     * @param args The bundle obtainable by the getArguments method.
     * @return true if arguments were read successfully, false otherwise.
     * Default implementation returns true.
     */
    protected boolean handleArguments(Bundle args) {
        return true;
    }

    /**
     * Create the presenter for this fragment
     */
    protected abstract T createPresenter();

    /**
     * Loads the view elements for the fragment
     */
    protected void setUi() {
        // Do nothing, ButterKnife does this for us now!
    }

    /**
     * Initializes any variables that the fragment needs
     */
    protected abstract void init();

    /**
     * Populates the view elements of the fragment
     */
    protected void populate() {
        // Do nothing, override if needed!
    }

    /**
     * Sets the listeners for the views of the fragment
     */
    protected void setListeners() {
        // Do nothing, override if needed!
    }

    @Override
    public void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    /**
     * @return Returns the presenter for this View
     */
    public T getPresenter() {
        return mPresenter;
    }

}
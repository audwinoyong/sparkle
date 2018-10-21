package com.mad.sparkle.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

/**
 * The factory class for the Navigation ViewModel.
 */
public class NavigationViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;

    /**
     * Constructor that creates the factory
     *
     * @param application the application
     */
    public NavigationViewModelFactory(Application application) {
        mApplication = application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new NavigationViewModel(mApplication);
    }
}


package com.mad.sparkle.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class RegisterViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;

    public RegisterViewModelFactory(Application application) {
        mApplication = application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new RegisterViewModel(mApplication);
    }
}

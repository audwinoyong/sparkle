package com.mad.sparkle.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;

/**
 * The factory class for the Login ViewModel.
 */
public class LoginViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;
    private FirebaseAuth mAuth;

    /**
     * Constructor that creates the factory
     *
     * @param application the application
     * @param auth        Firebase Authentication
     */
    public LoginViewModelFactory(Application application, FirebaseAuth auth) {
        mApplication = application;
        mAuth = auth;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new LoginViewModel(mApplication, mAuth);
    }
}

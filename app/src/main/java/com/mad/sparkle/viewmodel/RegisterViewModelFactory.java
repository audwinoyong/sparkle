//package com.mad.sparkle.viewmodel;
//
//import android.app.Application;
//import android.arch.lifecycle.ViewModel;
//import android.arch.lifecycle.ViewModelProvider;
//
//import com.google.firebase.auth.FirebaseAuth;
//
//public class RegisterViewModelFactory extends ViewModelProvider.NewInstanceFactory {
//    private Application mApplication;
//    private FirebaseAuth mAuth;
//
//    public RegisterViewModelFactory(Application application, FirebaseAuth auth) {
//        mApplication = application;
//        mAuth = auth;
//    }
//
//    @Override
//    public <T extends ViewModel> T create(Class<T> modelClass) {
//        return (T) new RegisterViewModel(mApplication, mAuth);
//    }
//}

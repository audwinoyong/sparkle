package com.mad.sparkle.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;

import com.mad.sparkle.utils.FirebaseRepository;

/**
 * ViewModel that handles the logic for navigation activity.
 */
public class NavigationViewModel extends ViewModel {

    private Application mApplication;
    private FirebaseRepository mFirebaseRepo;

    public NavigationViewModel(Application application) {
        mApplication = application;
        mFirebaseRepo = FirebaseRepository.getInstance();
    }
}

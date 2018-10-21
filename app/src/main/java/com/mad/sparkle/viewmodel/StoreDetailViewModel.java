package com.mad.sparkle.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;

import com.mad.sparkle.utils.FirebaseRepository;

/**
 * ViewModel that handles the logic for store detail activity.
 */
public class StoreDetailViewModel extends ViewModel {

    private Application mApplication;
    private FirebaseRepository mFirebaseRepo;

    /**
     * Constructor for StoreDetail ViewModel
     *
     * @param application The application
     */
    public StoreDetailViewModel(Application application) {
        mApplication = application;
        mFirebaseRepo = FirebaseRepository.getInstance();
    }
}

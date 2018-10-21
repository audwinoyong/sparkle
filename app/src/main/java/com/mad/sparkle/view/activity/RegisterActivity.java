package com.mad.sparkle.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import com.mad.sparkle.databinding.ActivityRegisterBinding;
import com.mad.sparkle.R;
import com.mad.sparkle.utils.Constants;
import com.mad.sparkle.viewmodel.RegisterNavigator;
import com.mad.sparkle.viewmodel.RegisterViewModel;
import com.mad.sparkle.viewmodel.RegisterViewModelFactory;

import static com.mad.sparkle.utils.Constants.REQUEST_IMAGE_CAPTURE;

/**
 * Register activity for users to create an account.
 */
public class RegisterActivity extends AppCompatActivity implements RegisterNavigator {

    private ActivityRegisterBinding mActivityRegisterBinding;
    private RegisterViewModel mRegisterViewModel;

    /**
     * Called when activity is first created.
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityRegisterBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        mRegisterViewModel = ViewModelProviders.of(this, new RegisterViewModelFactory(getApplication()))
                .get(RegisterViewModel.class);
        mRegisterViewModel.setRegisterNavigator(this);
        mActivityRegisterBinding.setRegisterViewModel(mRegisterViewModel);
    }

    /**
     * Open the gallery photo intent to upload the profile image.
     */
    @Override
    public void changeProfileImage() {
        Intent imageIntent = new Intent((Intent.ACTION_PICK));
        imageIntent.setType(Constants.TYPE_IMAGE);
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(imageIntent, REQUEST_IMAGE_CAPTURE);
    }

    /**
     * Returns the result of the gallery photo activity, whether upload photo is successful or not.
     *
     * @param requestCode the request code
     * @param resultCode  the result code
     * @param data        the uploaded intent data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mRegisterViewModel.handleActivityResult(requestCode, resultCode, data);
    }

}


package com.mad.sparkle.view;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import com.mad.sparkle.R;
import com.mad.sparkle.databinding.ActivityLoginBinding;
import com.mad.sparkle.model.User;
import com.mad.sparkle.viewmodel.LoginViewModel;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding mActivityLoginBinding;
    private LoginViewModel mLoginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        String emailString = mActivityLoginBinding.activityLoginEmailEt.getText().toString();
        String passwordString = mActivityLoginBinding.activityLoginPasswordEt.getText().toString();

        mLoginViewModel = new LoginViewModel(new User(), this, emailString, passwordString,
                mActivityLoginBinding.activityLoginEmailEt, mActivityLoginBinding.activityLoginPasswordEt, mActivityLoginBinding.activityLoginProgressBar);
        mActivityLoginBinding.setLoginViewModel(mLoginViewModel);
    }

}


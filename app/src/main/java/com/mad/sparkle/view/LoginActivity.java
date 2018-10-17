package com.mad.sparkle.view;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.mad.sparkle.R;
import com.mad.sparkle.databinding.ActivityLoginBinding;
import com.mad.sparkle.viewmodel.LoginViewModel;
import com.mad.sparkle.viewmodel.LoginViewModelFactory;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding mActivityLoginBinding;
    private LoginViewModel mLoginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mLoginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(getApplication(), FirebaseAuth.getInstance()))
                .get(LoginViewModel.class);
        mActivityLoginBinding.setLoginViewModel(mLoginViewModel);
    }

}


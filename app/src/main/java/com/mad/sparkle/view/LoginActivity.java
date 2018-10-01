package com.mad.sparkle.view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mad.sparkle.R;
import com.mad.sparkle.databinding.ActivityLoginBinding;
import com.mad.sparkle.model.User;
import com.mad.sparkle.viewmodel.UserViewModel;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding mActivityLoginBinding;
    private UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        String emailString = mActivityLoginBinding.activityLoginEmailEt.getText().toString();
        String passwordString = mActivityLoginBinding.activityLoginPasswordEt.getText().toString();

        mUserViewModel = new UserViewModel(new User(), this, emailString, passwordString,
                mActivityLoginBinding.activityLoginEmailEt, mActivityLoginBinding.activityLoginPasswordEt, mActivityLoginBinding.activityLoginProgressBar);
        mActivityLoginBinding.setLoginViewModel(mUserViewModel);
    }

}


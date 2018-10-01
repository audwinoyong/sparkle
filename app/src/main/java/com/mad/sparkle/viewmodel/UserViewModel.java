package com.mad.sparkle.viewmodel;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mad.sparkle.view.MainActivity;
import com.mad.sparkle.R;
import com.mad.sparkle.model.User;

public class UserViewModel extends BaseObservable {

    private String mEmail;
    private String mPassword;
    private String mFirstName;
    private String mLastName;
    private String mMobilePhone;

    private FirebaseAuth mAuth;
    private Context mContext;
    private EditText mEmailEt;
    private EditText mPasswordEt;
    private View mProgressView;

    public UserViewModel(User user, Context context, String email, String password, EditText emailEt, EditText passwordEt, View progressView) {
        this.mEmail = email;
        this.mPassword = password;
        this.mFirstName = user.mFirstName;
        this.mLastName = user.mLastName;
        this.mMobilePhone = user.mMobilePhone;
        this.mContext = context;
        this.mEmailEt = emailEt;
        this.mPasswordEt = passwordEt;
        this.mProgressView = progressView;
    }

    @Bindable
    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
        notifyPropertyChanged(R.id.activity_login_email_et);
    }

    @Bindable
    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
        notifyPropertyChanged(R.id.activity_login_password_et);
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getMobilePhone() {
        return mMobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        mMobilePhone = mobilePhone;
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        mAuth = FirebaseAuth.getInstance();

        // Reset errors.
        mEmailEt.setError(null);
        mPasswordEt.setError(null);

        // Store values at the time of the login attempt.
        String email = getEmail();
        String password = getPassword();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordEt.setError(mContext.getString(R.string.error_invalid_password));
            focusView = mPasswordEt;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailEt.setError(mContext.getString(R.string.error_field_required));
            focusView = mEmailEt;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailEt.setError(mContext.getString(R.string.error_invalid_email));
            focusView = mEmailEt;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            signIn(email, password);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOGIN", "signInWithEmail:success");

                            Toast.makeText(mContext, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();

                            Intent mainIntent = new Intent(mContext, MainActivity.class);
                            mContext.startActivity(mainIntent);

                            showProgress(false);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            showProgress(false);
                            mPasswordEt.setError(mContext.getString(R.string.error_incorrect_password));
                            mPasswordEt.requestFocus();
                        }

                    }
                });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public View.OnClickListener onLoginClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        };
    }

}

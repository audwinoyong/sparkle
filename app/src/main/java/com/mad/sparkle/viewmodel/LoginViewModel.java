package com.mad.sparkle.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;


import com.google.firebase.auth.FirebaseAuth;
import com.mad.sparkle.utils.FirebaseRepository;
import com.mad.sparkle.R;

import static com.mad.sparkle.utils.Constants.LOG_TAG;

public class LoginViewModel extends ViewModel {

    private Application mApplication;
    private FirebaseAuth mAuth;
    private FirebaseRepository mFirebaseRepo;

    public ObservableField<String> email = new ObservableField<String>();
    public ObservableField<String> password = new ObservableField<String>();
    public ObservableField<String> emailError = new ObservableField<String>();
    public ObservableField<String> passwordError = new ObservableField<String>();
    public ObservableField<Boolean> progressIsShown = new ObservableField<Boolean>();

    public LoginViewModel(Application application, FirebaseAuth auth) {
        mApplication = application;
        mAuth = auth;
        mFirebaseRepo = new FirebaseRepository();
    }

    /**
     * Attempts to sign in specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin(View v) {
        // Reset errors.
        emailError.set(null);
        passwordError.set(null);

        // Store values at the time of the login attempt.
        String email = this.email.get();
        String password = this.password.get();

        boolean cancel = false;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailError.set(mApplication.getString(R.string.error_field_required));
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailError.set(mApplication.getString(R.string.error_invalid_email));
            cancel = true;
        }

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            passwordError.set(mApplication.getString(R.string.error_field_required));
            cancel = true;
        } else if (!isPasswordValid(password)) {
            passwordError.set(mApplication.getString(R.string.error_invalid_password));
            cancel = true;
        }

        // There was no error
        if (!cancel) {
            // Run AsyncTask
            new LoginAsyncTask().execute();
            Log.d(LOG_TAG, "Attempting login");
        }
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    @BindingAdapter("app:errorText")
    public static void setErrorMessage(EditText editText, String errorMessage) {
        editText.setError(errorMessage);
    }

    private class LoginAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Show a progress spinner
            progressIsShown.set(true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // Sleep for 1 second
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Attempt authentication in Firebase
            mFirebaseRepo.signIn(email.get(), password.get(), mApplication);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Hide the progress spinner
            progressIsShown.set(false);
        }
    }

}

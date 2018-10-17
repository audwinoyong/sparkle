package com.mad.sparkle.viewmodel;

import android.app.Activity;
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
    public ObservableField<Boolean> isFinished = new ObservableField<Boolean>();

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
    public void attemptLogin(View view) {
        // Reset errors.
        emailError.set(null);
        passwordError.set(null);

        // Store values at the time of the login attempt.
        String email = this.email.get();
        String password = this.password.get();

        boolean allFieldsAreValid = validateFields(email, password);

        // There was no error
        if (allFieldsAreValid) {
            // Run AsyncTask
            new LoginAsyncTask().execute();
            Log.d(LOG_TAG, "Attempting login");
        }
    }

    private boolean validateFields(String email, String password) {
        boolean allFieldsAreValid = true;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailError.set(mApplication.getString(R.string.error_field_required));
            allFieldsAreValid = false;
        } else if (!isEmailValid(email)) {
            emailError.set(mApplication.getString(R.string.error_invalid_email));
            allFieldsAreValid = false;
        }

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            passwordError.set(mApplication.getString(R.string.error_field_required));
            allFieldsAreValid = false;
        } else if (!isPasswordValid(password)) {
            passwordError.set(mApplication.getString(R.string.error_invalid_password));
            allFieldsAreValid = false;
        }

        return allFieldsAreValid;
    }

    /**
     * Check if email is in valid format
     *
     * @param email email address
     * @return whether it is in valid format
     */
    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Check if password in in valid format
     *
     * @param password password
     * @return whether it is in valid format
     */
    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    @BindingAdapter("app:errorText")
    public static void setErrorMessage(EditText editText, String errorMessage) {
        editText.setError(errorMessage);
    }

    @BindingAdapter("app:onFinish")
    public static void finishActivity(View view, boolean isFinished) {
//        Log.d(LOG_TAG, "finishActivity is called");
//        if (isFinished) {
//            ((Activity) (view.getContext())).finish();
//            Log.d(LOG_TAG, "Login activity is finished");
//        }
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
//            isFinished.set(true);
        }
    }

}

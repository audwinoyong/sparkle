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

/**
 * ViewModel that handles the logic for login activity.
 */
public class LoginViewModel extends ViewModel {

    private Application mApplication;
    private FirebaseAuth mAuth;
    private FirebaseRepository mFirebaseRepo;

    public ObservableField<String> email = new ObservableField<String>();
    public ObservableField<String> password = new ObservableField<String>();

    public ObservableField<String> emailError = new ObservableField<String>();
    public ObservableField<String> passwordError = new ObservableField<String>();

    public ObservableField<Boolean> progressIsShown = new ObservableField<Boolean>();

    /**
     * Constructor for Login ViewModel
     *
     * @param application The application
     * @param auth        Firebase Authentication
     */
    public LoginViewModel(Application application, FirebaseAuth auth) {
        mApplication = application;
        mAuth = auth;
        mFirebaseRepo = FirebaseRepository.getInstance();
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

    /**
     * Validate all the fields
     *
     * @param email    the email
     * @param password the password
     * @return true if all fields are valid and vice versa
     */
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
     * Check if password is in valid format
     *
     * @param password password
     * @return whether it is in valid format
     */
    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    /**
     * Set the EditText error message using binding adapter
     *
     * @param editText     the EditText
     * @param errorMessage the error message
     */
    @BindingAdapter("app:errorText")
    public static void setErrorMessage(EditText editText, String errorMessage) {
        editText.setError(errorMessage);
    }

    /**
     * AsyncTask to attempt login.
     * Show the progress bar during the process.
     */
    private class LoginAsyncTask extends AsyncTask<Void, Void, Void> {

        /**
         * Show the progress bar
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Show a progress spinner
            progressIsShown.set(true);
        }

        /**
         * Attempt login using Firebase Authentication
         *
         * @param voids void
         * @return null
         */
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

        /**
         * Hide the progress bar
         *
         * @param aVoid void
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Hide the progress spinner
            progressIsShown.set(false);
        }
    }

}

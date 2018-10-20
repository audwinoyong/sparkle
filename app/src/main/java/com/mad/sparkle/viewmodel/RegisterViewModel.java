package com.mad.sparkle.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mad.sparkle.R;
import com.mad.sparkle.utils.Constants;
import com.mad.sparkle.utils.FirebaseRepository;

import java.lang.ref.WeakReference;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.mad.sparkle.utils.Constants.LOG_TAG;
import static com.mad.sparkle.utils.Constants.REQUEST_IMAGE_CAPTURE;

public class RegisterViewModel extends ViewModel {

    private Application mApplication;
    private FirebaseRepository mFirebaseRepo;
    private WeakReference<RegisterNavigator> mRegisterNavigator;

    public ObservableField<Uri> imageUri = new ObservableField<Uri>();

    public ObservableField<String> email = new ObservableField<String>();
    public ObservableField<String> password = new ObservableField<String>();
    public ObservableField<String> firstName = new ObservableField<String>();
    public ObservableField<String> lastName = new ObservableField<String>();
    public ObservableField<String> mobilePhone = new ObservableField<String>();

    public ObservableField<String> emailError = new ObservableField<String>();
    public ObservableField<String> passwordError = new ObservableField<String>();
    public ObservableField<String> firstNameError = new ObservableField<String>();
    public ObservableField<String> lastNameError = new ObservableField<String>();
    public ObservableField<String> mobilePhoneError = new ObservableField<String>();

    public ObservableField<Boolean> fabIsShown = new ObservableField<Boolean>();
    public ObservableField<Boolean> progressIsShown = new ObservableField<Boolean>();

    public RegisterViewModel(Application application) {
        mApplication = application;
        mFirebaseRepo = FirebaseRepository.getInstance();

        fabIsShown.set(true);
    }

    public void setRegisterNavigator(RegisterNavigator registerNavigator) {
        mRegisterNavigator = new WeakReference<>(registerNavigator);
    }

    public void attemptRegister(View view) {
        // Reset errors.
        emailError.set(null);
        passwordError.set(null);
        firstNameError.set(null);
        lastNameError.set(null);
        mobilePhoneError.set(null);

        // Store values at the time of the register attempt.
        String email = this.email.get();
        String password = this.password.get();
        String firstName = this.firstName.get();
        String lastName = this.lastName.get();
        String mobilePhone = this.mobilePhone.get();

        boolean allFieldsAreValid = validateFields(email, password, firstName, lastName, mobilePhone);

        // There was no error
        if (allFieldsAreValid) {
            // Run AsyncTask
            new RegisterAsyncTask().execute();
            Log.d(LOG_TAG, "Attempting register");
        }
    }

    private boolean validateFields(String email, String password, String firstName, String lastName, String mobilePhone) {
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

        // Check for a valid first name.
        if (TextUtils.isEmpty(firstName)) {
            firstNameError.set(mApplication.getString(R.string.error_field_required));
            allFieldsAreValid = false;
        } else if (!isNameValid(firstName)) {
            firstNameError.set(mApplication.getString(R.string.error_invalid_name));
            allFieldsAreValid = false;
        }

        // Check for a valid last name.
        if (TextUtils.isEmpty(lastName)) {
            lastNameError.set(mApplication.getString(R.string.error_field_required));
            allFieldsAreValid = false;
        } else if (!isNameValid(lastName)) {
            lastNameError.set(mApplication.getString(R.string.error_invalid_name));
            allFieldsAreValid = false;
        }

        // Check for a valid mobile phone.
        if (TextUtils.isEmpty(mobilePhone)) {
            mobilePhoneError.set(mApplication.getString(R.string.error_field_required));
            allFieldsAreValid = false;
        } else if (!isPhoneValid(mobilePhone)) {
            mobilePhoneError.set(mApplication.getString(R.string.error_invalid_phone));
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

    private boolean isNameValid(String name) {
        return name.matches(Constants.REGEX_NAME);
    }

    private boolean isPhoneValid(String phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }

    @BindingAdapter("app:errorText")
    public static void setErrorMessage(EditText editText, String errorMessage) {
        editText.setError(errorMessage);
    }

    @BindingAdapter("app:imageUri")
    public static void setImageURI(CircleImageView circleImageView, Uri imageUri) {
        circleImageView.setImageURI(imageUri);
    }

    @BindingAdapter("app:onFinish")
    public static void finishActivity(View view, boolean isFinished) {
//        Log.d(LOG_TAG, "finishActivity is called");
//        if (isFinished) {
//            ((Activity) (view.getContext())).finish();
//            Log.d(LOG_TAG, "Register activity is finished");
//        }
    }

    public void onProfileImageClick(View view) {
        mRegisterNavigator.get().changeProfileImage();
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageUri.set(data.getData());
            fabIsShown.set(false);

            Toast.makeText(mApplication.getApplicationContext(), mApplication.getString(R.string.upload_image_successful), Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, "Upload image to form successful");
        }
    }

    private class RegisterAsyncTask extends AsyncTask<Void, Void, Void> {

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

            // Attempt registration in Firebase
            mFirebaseRepo.register(email.get(), password.get(), firstName.get(), lastName.get(), mobilePhone.get(), imageUri.get(), mApplication);
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

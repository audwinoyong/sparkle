package com.mad.sparkle.view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mad.sparkle.utils.Constants;
import com.mad.sparkle.R;
import com.mad.sparkle.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.mad.sparkle.utils.Constants.PROFILE_IMAGE;
import static com.mad.sparkle.utils.Constants.REQUEST_IMAGE_CAPTURE;
import static com.mad.sparkle.utils.Constants.LOG_TAG;
import static com.mad.sparkle.utils.Constants.USERS;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity {

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;

    // UI references.
    private CircleImageView mProfileImg;
    private FloatingActionButton mProfileImgFab;
    private EditText mEmailEt;
    private EditText mPasswordEt;
    private EditText mFirstNameEt;
    private EditText mLastNameEt;
    private EditText mMobilePhoneEt;
    private Uri mImageUri;

    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        // Set up the login form.
        mProfileImg = (CircleImageView) findViewById(R.id.activity_register_profile_image);
        mProfileImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfileImage();
            }
        });

        mProfileImgFab = (FloatingActionButton) findViewById(R.id.activity_register_profile_image_fab);

        mEmailEt = (EditText) findViewById(R.id.activity_register_email);

        mPasswordEt = (EditText) findViewById(R.id.activity_register_password);
        mPasswordEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        mFirstNameEt = findViewById(R.id.activity_register_first_name_et);
        mLastNameEt = findViewById(R.id.activity_register_last_name_et);
        mMobilePhoneEt = findViewById(R.id.activity_register_mobile_phone_et);

        Button mEmailRegisterButton = (Button) findViewById(R.id.activity_register_register_button);
        mEmailRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mLoginFormView = findViewById(R.id.activity_register_login_form);
        mProgressView = findViewById(R.id.activity_register_login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        // Reset errors.
        mEmailEt.setError(null);
        mPasswordEt.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailEt.getText().toString();
        String password = mPasswordEt.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordEt.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordEt;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailEt.setError(getString(R.string.error_field_required));
            focusView = mEmailEt;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailEt.setError(getString(R.string.error_invalid_email));
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
            register(email, password);
        }
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

    private void register(final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOG_TAG, "createUserWithEmail:success");

                            Toast.makeText(RegisterActivity.this, "Registration successful.",
                                    Toast.LENGTH_SHORT).show();

                            String firstName = mFirstNameEt.getText().toString();
                            String lastName = mLastNameEt.getText().toString();
                            String mobilePhone = mMobilePhoneEt.getText().toString();

                            User newUser = new User(email, password, firstName, lastName, mobilePhone, "");

                            String uid = mAuth.getCurrentUser().getUid();
                            FirebaseDatabase.getInstance().getReference(Constants.USERS)
                                    .child(uid).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(LOG_TAG, "registerToDatabase:success");

                                        Intent navigationIntent = new Intent(RegisterActivity.this, NavigationActivity.class);
                                        startActivity(navigationIntent);
                                    } else {
                                        Log.w(LOG_TAG, "registerToDatabase:failure", task.getException());
                                    }
                                }
                            });

                            // Store profile image to Storage and Database
                            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child(USERS).child(uid);
                            StorageReference filepath = mStorageRef.child(USERS).child(uid).child(mImageUri.getLastPathSegment());

                            filepath.putFile(mImageUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // Get the download Url from Storage and save it to the Database
                                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri downloadUrl) {
                                                    mDatabaseRef.child(PROFILE_IMAGE).setValue(downloadUrl.toString());
                                                    Log.d(LOG_TAG, "uploadImageToStorage:success");
                                                }
                                            });

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            Log.d(LOG_TAG, "uploadImageToStorage:failure");
                                        }
                                    });

                            showProgress(false);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(LOG_TAG, "createUserWithEmail:failure", task.getException());
                            showProgress(false);

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(RegisterActivity.this, "Email already exists",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
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

    private void changeProfileImage() {
        Intent imageIntent = new Intent((Intent.ACTION_PICK));
        imageIntent.setType("image/*");
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(imageIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            mProfileImg.setImageURI(mImageUri);
            Toast.makeText(RegisterActivity.this, "Upload image successful", Toast.LENGTH_SHORT).show();
            mProfileImgFab.hide();
            Log.d(LOG_TAG, "Upload image successful");
        }

    }
}


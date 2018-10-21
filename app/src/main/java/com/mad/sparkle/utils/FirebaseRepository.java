package com.mad.sparkle.utils;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
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
import com.mad.sparkle.R;
import com.mad.sparkle.model.User;
import com.mad.sparkle.view.activity.NavigationActivity;

import static com.mad.sparkle.utils.Constants.LOG_TAG;
import static com.mad.sparkle.utils.Constants.PROFILE_IMAGE;
import static com.mad.sparkle.utils.Constants.USERS;

/**
 * Firebase repository class contains all Firebase related codes as the data access layer.
 */
public class FirebaseRepository {

    // Singleton pattern field
    private static FirebaseRepository sInstance;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    /**
     * Singleton pattern to return an instance of Firebase Repository.
     *
     * @return FirebaseRepository instance
     */
    public static synchronized FirebaseRepository getInstance() {
        if (sInstance == null) {
            sInstance = new FirebaseRepository();
        }
        return sInstance;
    }

    /**
     * Empty constructor
     */
    private FirebaseRepository() {
    }

    /**
     * Get the current user database reference.
     *
     * @param uid user key
     * @return database reference
     */
    private DatabaseReference getCurrentUserDatabaseReference(String uid) {
        return FirebaseDatabase.getInstance().getReference().child(USERS).child(uid);
    }

    /**
     * Get the current user storage reference.
     *
     * @param uid user key
     * @return storage reference
     */
    private StorageReference getCurrentUserStorageReference(String uid) {
        return FirebaseStorage.getInstance().getReference().child(USERS).child(uid);
    }

    /**
     * Attempt sign in using Firebase Authentication.
     *
     * @param email       user email
     * @param password    user password
     * @param application application
     */
    public void signIn(final String email, final String password, final Application application) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, start navigation activity
                            Toast.makeText(application.getApplicationContext(), R.string.sign_in_successful,
                                    Toast.LENGTH_SHORT).show();

                            Intent navigationIntent = new Intent(application.getApplicationContext(), NavigationActivity.class);
                            navigationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            application.startActivity(navigationIntent);

                            Log.d(LOG_TAG, "signInWithEmail:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(application.getApplicationContext(), R.string.error_incorrect_email_or_password,
                                    Toast.LENGTH_SHORT).show();

                            Log.d(LOG_TAG, "signInWithEmail:failure");
                        }
                    }
                });
    }

    /**
     * Attempt registration to Firebase.
     *
     * @param email       user email
     * @param password    user password
     * @param firstName   user first name
     * @param lastName    user last name
     * @param mobilePhone user mobile phone
     * @param imageUri    profile image uri
     * @param application application
     */
    public void register(final String email, final String password, final String firstName, final String lastName, final String mobilePhone,
                         final Uri imageUri, final Application application) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Create user authentication success
                            Log.d(LOG_TAG, "createUserWithEmail:success");

                            // Create new User object
                            String uid = mAuth.getCurrentUser().getUid();
                            User newUser = new User(email, password, firstName, lastName, mobilePhone, "");

                            // Add the new user to Database
                            addUserToDatabase(uid, newUser, application);

                            // If there is an uploaded image, save it to the Storage
                            if (imageUri != null) {
                                uploadImageToStorage(uid, imageUri);
                            }

                            Toast.makeText(application.getApplicationContext(), R.string.registration_successful,
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // If registration fails, display a message to the user.
                            Log.d(LOG_TAG, "createUserWithEmail:failure", task.getException());

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(application.getApplicationContext(), R.string.email_already_exists,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(application.getApplicationContext(), task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    /**
     * Add the user to Firabase Database.
     *
     * @param uid         user key
     * @param newUser     user object
     * @param application application
     */
    private void addUserToDatabase(String uid, User newUser, final Application application) {
        getCurrentUserDatabaseReference(uid).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "addUserToDatabase:success");

                    // Add user to database successful, start navigation activity
                    Intent navigationIntent = new Intent(application.getApplicationContext(), NavigationActivity.class);
                    navigationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    application.startActivity(navigationIntent);
                } else {
                    Log.d(LOG_TAG, "addUserToDatabase:failure", task.getException());
                }
            }
        });
    }

    /**
     * Upload image to Firebase Storage.
     *
     * @param uid      user key
     * @param imageUri image uri
     */
    private void uploadImageToStorage(final String uid, Uri imageUri) {
        // Store profile image to Storage and Database
        StorageReference filepath = getCurrentUserStorageReference(uid).child(imageUri.getLastPathSegment());
        filepath.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get the download Url from Storage and save it to the Database
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUrl) {
                                getCurrentUserDatabaseReference(uid).child(PROFILE_IMAGE).setValue(downloadUrl.toString());
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
    }

}

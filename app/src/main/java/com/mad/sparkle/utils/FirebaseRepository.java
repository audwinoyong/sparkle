package com.mad.sparkle.utils;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mad.sparkle.R;
import com.mad.sparkle.view.NavigationActivity;

import static com.mad.sparkle.utils.Constants.TAG;

public class FirebaseRepository {

    public void signIn(final String email, final String password, final Application application) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, start navigation activity
                            Toast.makeText(application.getApplicationContext(), R.string.sign_in_successful,
                                    Toast.LENGTH_SHORT).show();

                            Intent mainIntent = new Intent(application.getApplicationContext(), NavigationActivity.class);
                            application.startActivity(mainIntent);

                            Log.d(TAG, "signInWithEmail:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(application.getApplicationContext(), R.string.error_incorrect_email_or_password,
                                    Toast.LENGTH_SHORT).show();

                            Log.d(TAG, "signInWithEmail:failure");
                        }
                    }
                });
    }

//    public void register(final String email, final String password, final Activity activity) {
//        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            registerSuccess = true;
//                            Log.d("REGISTER", "createUserWithEmail:success");
//
//                            Toast.makeText(RegisterActivity.this, "Registration successful.",
//                                    Toast.LENGTH_SHORT).show();
//
//                            String firstName = mFirstNameEt.getText().toString();
//                            String lastName = mLastNameEt.getText().toString();
//                            String mobilePhone = mMobilePhoneEt.getText().toString();
//
//                            User newUser = new User(email, password, firstName, lastName, mobilePhone);
//
//                            String uid = mAuth.getCurrentUser().getUid();
//                            FirebaseDatabase.getInstance().getReference(Constants.USERS)
//                                    .child(uid).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Log.d("REGISTER_DATABASE", "registerToDatabase:success");
//
//                                        Toast.makeText(RegisterActivity.this, "Registration into database successful.",
//                                                Toast.LENGTH_SHORT).show();
//
//                                        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
//                                        startActivity(loginIntent);
//                                    } else {
//                                        Log.w("REGISTER_DATABASE", "registerToDatabase:failure", task.getException());
//                                        Toast.makeText(RegisterActivity.this, "Registration into database failed.",
//                                                Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//
//                            showProgress(false);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w("REGISTER", "createUserWithEmail:failure", task.getException());
//                            showProgress(false);
//
//                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
//                                Toast.makeText(RegisterActivity.this, "Email already exists",
//                                        Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(),
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
//    }

}

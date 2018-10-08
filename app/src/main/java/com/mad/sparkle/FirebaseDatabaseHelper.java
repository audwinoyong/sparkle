//package com.mad.sparkle;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthUserCollisionException;
//import com.google.firebase.database.FirebaseDatabase;
//import com.mad.sparkle.model.User;
//import com.mad.sparkle.view.LoginActivity;
//import com.mad.sparkle.view.NavigationActivity;
//import com.mad.sparkle.view.RegisterActivity;
//
//public class FirebaseDatabaseHelper {
//
//    public boolean signInSuccess;
//    public boolean registerSuccess;
//
//    public boolean signIn(String email, String password) {
//        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            signInSuccess = true;
//                            Log.d("LOGIN", "signInWithEmail:success");
//                        } else {
//                            signInSuccess = false;
//                            Log.d("LOGIN", "signInWithEmail:failure");
//
//                        }
//                    }
//                });
//        return signInSuccess;
//    }
//
//    public boolean register(final String email, final String password, final Activity activity) {
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
//
//}

package com.mazimia.mobile.nurselectureroom;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 321;
    Button logInButton;
    FirebaseUser currentUser;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // handle the click event of the log in button
        logInButton = findViewById(R.id.loginButton);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                updateUI();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        // ensure that authentication mechanism kicks off from this activity
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Authentication successful", Toast.LENGTH_SHORT)
                        .show();
                // get the current user after log in

                OnSuccessListener<Void> success = new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this,
                                "Successfully created user",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                };

                OnFailureListener failure = new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,
                                "Couldn't create user. Sign up with your email account",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                };
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                Map<String, Object> user = new HashMap<>();
                user.put("displayName", currentUser.getDisplayName());
                user.put("email", currentUser.getEmail());
                user.put("token", currentUser.getUid());
                new FireStoreUtil(FirebaseFirestore.getInstance()).createUser(user, success, failure);

                // update the UI to the next page

                updateUI();
                finish();
            } else {
                // show the error message to the user via toast
                if (response == null) {
                    Toast.makeText(this, "Could not complete authentication", Toast.LENGTH_LONG).show();
                } else {
                    String errorInfo = response.getError().getMessage();
                    Toast.makeText(this, errorInfo, Toast.LENGTH_LONG).show();
                }

            }
        }

    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), "Tap back button again to to exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }

    // Toggle user between auth page and welcome page
    private void updateUI() {
        // log the user in if they've been authenticated otherwise
        // show the auth page
        if (currentUser != null) {
            logUserIn(currentUser);
        } else {// show the auth page from firebase ui
            authenticateUser();
        }

    }


    // Authenticate a user
    private void authenticateUser() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
        );
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(), RC_SIGN_IN);
    }


    // Navigate a user to the landing page after authentication
    private void logUserIn(FirebaseUser currentUser) {
        Intent startIntent = new Intent(this, WelcomeActivity.class);
        startIntent.putExtra("userInfo", currentUser);
        startActivity(startIntent);
    }
}

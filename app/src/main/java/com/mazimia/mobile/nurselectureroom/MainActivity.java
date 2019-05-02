package com.mazimia.mobile.nurselectureroom;

import android.content.Intent;
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

    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 321;
    Button logInButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);

        // handle the click event of the log in button
        logInButton = findViewById(R.id.loginButton);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI(mAuth.getCurrentUser());
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
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
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
                Map<String, Object> user = new HashMap<>();
                user.put("displayName", currentUser.getDisplayName());
                user.put("email", currentUser.getEmail());
                user.put("token", currentUser.getUid());
                new FireStoreUtil(FirebaseFirestore.getInstance()).createUser(user, success, failure);

                // update the UI to the next page

                updateUI(FirebaseAuth.getInstance().getCurrentUser());
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

    // Toggle user between auth page and welcome page
    private void updateUI(FirebaseUser currentUser) {
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

package com.mazimia.mobile.nurselectureroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class NurseRoomActivity extends SignOutActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse_room);
    }

    @Override
    protected void onStart() {
        // check if the user is authenticated and take necessary actions
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}

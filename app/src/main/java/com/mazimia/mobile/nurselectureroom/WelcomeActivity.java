package com.mazimia.mobile.nurselectureroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends SignOutActivity {

    private Button nrButton;
    private Button highScoreButton;
    private Button feedBackButton;
    private Button aboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        nrButton = findViewById(R.id.nrButton);
        highScoreButton = findViewById(R.id.highscoreBtn);
        feedBackButton = findViewById(R.id.feedbackBtn);
        aboutButton = findViewById(R.id.aboutBtn);

        nrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNurseRoomActivity();
            }
        });


        highScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHighScoreActivity();
            }
        });


        feedBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFeedbackActivity();
            }
        });


        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutActivity();
            }
        });
    }


    // Navigate to the about page
    private void aboutActivity() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }


    // Navigate to the feedback page
    private void openFeedbackActivity() {
        Intent intent = new Intent(this, FeedbackActivity.class);
        startActivity(intent);
    }


    // Navigate to the high score page
    private void openHighScoreActivity() {
        Intent intent = new Intent(this, HighScoreActivity.class);
        startActivity(intent);
    }


    // Navigate to the nurse lecture room page
    private void openNurseRoomActivity() {
        Intent intent = new Intent(this, NurseRoomActivity.class);
        startActivity(intent);
    }
}

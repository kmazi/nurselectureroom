package com.mazimia.mobile.nurselectureroom;

import android.content.Intent;
import android.net.Uri;
import android.opengl.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class WelcomeActivity extends SignOutActivity {

    private Button studyBtn;
    private Button highScoreButton;
    private Button feedBackButton;
    private Button aboutBtn;
    private TextView userProfile;
    private TextView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        userProfile = findViewById(R.id.welcome_textview);
        userImage = findViewById(R.id.name_avatar_textview);
        studyBtn = findViewById(R.id.studyBtn);
        aboutBtn = findViewById(R.id.aboutBtn);
        feedBackButton = findViewById(R.id.fback_btn);

        Intent welcomeIntent = getIntent();
        FirebaseUser currentUser = welcomeIntent.getParcelableExtra("userInfo");
        String userName = currentUser.getDisplayName();
        String firstName = userName.split(" ")[0];
        userProfile.setText(userProfile.getText() + " " + firstName);

        userImage = findViewById(R.id.name_avatar_textview);
        Character firstInitial = userName.charAt(0);
        Character secondInitial = userName.charAt(userName.indexOf(" ") + 1);
        String initials = firstInitial.toString()+secondInitial.toString();
        userImage.setText(initials);

        studyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNurseRoomActivity();
            }
        });

        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutActivity();
            }
        });

        feedBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFeedbackActivity();
            }
        });

//        CircleImageView avatar = findViewById(R.id.profile_image);
//        avatar.setVisibility(View.GONE);
//
//        Glide.with(WelcomeActivity.this).load(currentUser.getPhotoUrl())
//                .into(avatar);
//        avatar.setVisibility(View.VISIBLE);
//        userImage.setVisibility(View.GONE);
//        nrButton = findViewById(R.id.nrButton);
//        highScoreButton = findViewById(R.id.highscoreBtn);
//        feedBackButton = findViewById(R.id.feedbackBtn);
//        aboutButton = findViewById(R.id.aboutBtn);
//

//
//
//        highScoreButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openHighScoreActivity();
//            }
//        });
//

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


    // Navigate to the about page
    private void aboutActivity() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }


    // Navigate to the feedback page
    private void openFeedbackActivity() {
//        Intent intent = new Intent(this, FeedbackActivity.class);
//        startActivity(intent);

        Intent email = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"nurse.lectureroom@gmail.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "Give us feedback");
//        email.setType("application/octet-stream");
        startActivity(Intent.createChooser(email, "Choose an email client.."));
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

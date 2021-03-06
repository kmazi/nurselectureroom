package com.mazimia.mobile.nurselectureroom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        String packageInstaller = getPackageManager().getInstallerPackageName(getPackageName());

        if (packageInstaller == null || !packageInstaller.equals("com.android.vending")) {
            AlertDialog alert = new AlertDialog.Builder(this)
            .setTitle("Information")
            .setIcon(R.drawable.ic_launcher_background)
            .setMessage("There's an updated version out there! uninstall your current version"+
                    "  and then navigate to Google Play Store and search for simple nursing to get it.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something
                        String url = "https://play.google.com/store/apps/details?id=com.mazimia.mobile.nurselectureroom";
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        finish();
                    }
                })
            .create();
            alert.setCancelable(false);
            alert.setCanceledOnTouchOutside(false);
            alert.show();

        }

        // Initialize the views created in the activity_welcome.xml file
        userProfile = findViewById(R.id.welcome_textview);
        userImage = findViewById(R.id.name_avatar_textview);
        studyBtn = findViewById(R.id.studyBtn);
        aboutBtn = findViewById(R.id.aboutBtn);
        feedBackButton = findViewById(R.id.fback_btn);
        highScoreButton = findViewById(R.id.highscoreBtn);

        Intent welcomeIntent = getIntent();
        FirebaseUser currentUser = welcomeIntent.getParcelableExtra("userInfo");
        if (currentUser != null) {
            String[] names = currentUser.getDisplayName().split(" ");
            String firstName = names[0];
            userProfile.setText(userProfile.getText() + " " + firstName);

            Character firstInitial = names[0].charAt(0);
            Character secondInitial;
            String initials;
            if (names.length > 1) {
                secondInitial = names[1].charAt(0);
                initials = firstInitial.toString() + secondInitial.toString();
            } else {
                initials = firstInitial.toString();
            }

            userImage.setText(initials);
        }

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

        highScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHighScoreActivity();
            }
        });
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

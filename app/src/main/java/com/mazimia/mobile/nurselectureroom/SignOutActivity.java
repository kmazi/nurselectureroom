package com.mazimia.mobile.nurselectureroom;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignOutActivity extends AppCompatActivity {

    private FireStoreUtil storeUtil;
    private LectureSection lectureSec;
    private Lecture lecture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_out);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        storeUtil = new FireStoreUtil(db);
        lectureSec = new LectureSection(
                "AIDS is real in this part of the world",
                "Blood is essential in the live of humans",
                "doc/start/jump/" + Math.random());

        lecture = new Lecture("Chest ache",
                "Chest pain is not an easy thing down here. It might just lead to death",
                "Please don't try this at home");
        lecture.setLecture();
        storeUtil.updateLectureSection("AIDS39", lectureSec);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // inflate the sign out menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.signout_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Action when sign out is selected.
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                signOutUser();
                return true;

            case R.id.add_section_menu:
                storeUtil.createLectureSection(lectureSec, new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference aVoid) {
                        Toast.makeText(SignOutActivity.this, "Successfully deleted the section", Toast.LENGTH_LONG);
                    }
                });
                return true;

            case R.id.add_lecture_menu:
                storeUtil.updateLecture(lectureSec, lecture, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SignOutActivity.this, "Successfully deleted the section", Toast.LENGTH_LONG);
                    }
                });
                return true;
        }
        return true;
    }

    private void signOutUser() {
        Task<Void> voidTask = AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent mainActivityIntent = new Intent(SignOutActivity.this, MainActivity.class);
                        startActivity(mainActivityIntent);
                    }
                });
    }
}

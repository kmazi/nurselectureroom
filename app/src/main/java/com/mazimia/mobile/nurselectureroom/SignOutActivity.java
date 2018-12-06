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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignOutActivity extends AppCompatActivity {

    private FireStoreUtil storeUtil;
    private Section lectureSec;
    private Lecture lecture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_out);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        storeUtil = new FireStoreUtil(db);
        lectureSec = new Section(
                "Heart Disease",
                "Blood is essential in the live of humans",
                "doc/start/jump/" + Math.random());

        lecture = new Lecture("Back pain",
                "Pain is pain, whether back or front. It might just lead to death",
                "Please it doesn't matter");
        lecture.setId("z3gmjYiF9PREeJyH6XQI");
        storeUtil.getLecture(lecture);
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
                storeUtil.createLectureSection(lectureSec, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // do something when its successful
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // do something when operation fails
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

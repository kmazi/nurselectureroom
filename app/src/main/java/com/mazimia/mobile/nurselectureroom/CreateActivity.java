package com.mazimia.mobile.nurselectureroom;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.MessageFormat;

public class CreateActivity extends AppCompatActivity {

    // ALl the widgets to use in this activity
    private EditText title;
    private TextView caption;
    private EditText summary;
    private EditText note;
    private Button createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        title = findViewById(R.id.sec_lec_title);
        caption = findViewById(R.id.heading_txt);
        summary = findViewById(R.id.sec_lec_summary);
        note = findViewById(R.id.lec_note);
        createBtn = findViewById(R.id.create_btn);

        note.setVisibility(View.GONE);

        // Know whether to create a section or a lecture from intent extra
        final String action = getIntent().getStringExtra("isCreate");
        final String id = getIntent().getStringExtra("sectionId");
        final String lecId = getIntent().getStringExtra("lectureId");
        final String title = getIntent().getStringExtra("title");
        final String summary = getIntent().getStringExtra("summary");
        String lecNote = getIntent().getStringExtra("note");
        final boolean isEdit = getIntent().getBooleanExtra("isEdit", false);

        // Check to see if the user intend to edit an existing section
        if (isEdit && !title.equals("") && !summary.equals("")) {
            createBtn.setText("Edit");
            this.title.setText(title);
            this.summary.setText(summary);
            this.note.setText(lecNote);
        }

        if(action.equals("lecture")) {
            note.setVisibility(View.VISIBLE);
        }

        Object[] objects= {action};
        MessageFormat fmt;
        if (isEdit) {
            fmt = new MessageFormat("Edit a {0}");
        } else {
            fmt = new MessageFormat("Create a {0}");
        }
        caption.setText(fmt.format(objects));

        // handles the event of creating whether section or lecture
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireStoreUtil storeUtil = new FireStoreUtil(FirebaseFirestore.getInstance());
                String getTitle = CreateActivity.this.title.getText().toString();
                String getNote = note.getText().toString();
                String getSummary = CreateActivity.this.summary.getText().toString();

                if (action.equals("section") && !getTitle.equals("") && !getSummary.equals("")) {

                    // Check if the text boxes are loaded already in which case it's an edit operation
                    if (isEdit) {

                        Section section = new Section(getTitle, getSummary);
                        section.setId(id);

                        // Successfully updated a section
                        OnSuccessListener<Void> success = new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                clearText();
                                // do something when successful
                                Toast.makeText(CreateActivity.this,
                                        "Update was successful!",
                                        Toast.LENGTH_LONG).show();
                            }
                        };

                        // Failure to update a section.
                        OnFailureListener failure = new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CreateActivity.this,
                                        "Update was unsuccessful, check your internet connection",
                                        Toast.LENGTH_LONG).show();
                            }
                        };

                        // call the function to update section
                        storeUtil.updateSection(section, success, failure);
                    } else {
                        // Creating new section
                        Section section = new Section(getTitle, getSummary);

                        // On successful creation
                        OnSuccessListener<Void> success = new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                clearText();
                                Toast.makeText(CreateActivity.this,
                                        "Successfully created a section!",
                                        Toast.LENGTH_LONG).show();
                            }
                        };

                        // On failure to create section
                        OnFailureListener failure = new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CreateActivity.this,
                                        "Could not create a section! Check your internet connection",
                                        Toast.LENGTH_LONG).show();
                            }
                        };

                        // call the function to create section
                        storeUtil.createLectureSection(section, success, failure);
                    }

                } // If operation isn't for sections it could be for lectures.
                else if (action.equals("lecture") && !getTitle.equals("")
                        && !getSummary.equals("") && !getNote.equals("")) {

                    if (isEdit) {

                        Lecture lecture = new Lecture(getTitle, getSummary, getNote);
                        lecture.setId(lecId);

                        OnSuccessListener success = new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(CreateActivity.this,
                                        "Successfully updated the lecture!",
                                        Toast.LENGTH_LONG).show();
                                clearText();
                            }
                        };

                        OnFailureListener failure = new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CreateActivity.this,
                                        "Unable to edit lecture. Please try again",
                                        Toast.LENGTH_LONG).show();
                            }
                        };
                        // update a lecture
                        storeUtil.updateLecture(lecture, success, failure);

                    } else {

                        Lecture lecture = new Lecture(getTitle, getSummary, getNote);
                        lecture.setSectionId(id);

                        // handles success in creation of lecture
                        OnSuccessListener success = new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(CreateActivity.this,
                                                        "Successfully created a lecture!",
                                                        Toast.LENGTH_LONG).show();
                                                clearText();
                                            }
                                        };

                        // on failure
                        OnFailureListener failure = new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // do something when doc creation fails.
                                Toast.makeText(CreateActivity.this,
                                        "Could not create a lecture! Check your internet connection",
                                        Toast.LENGTH_LONG).show();
                            }
                        };

                        // Create a new lecture
                        storeUtil.createLecture(lecture, success, failure);
                    }


                } else {
                    Toast.makeText(CreateActivity.this,
                            "Give a title, summary or note before submitting",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    void clearText() {
        title.setText("");
        summary.setText("");
        note.setText("");
    }
}

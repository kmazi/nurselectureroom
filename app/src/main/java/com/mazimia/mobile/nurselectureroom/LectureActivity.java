package com.mazimia.mobile.nurselectureroom;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LectureActivity extends AppCompatActivity {

    private TextView title;
    private TextView note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);

        title = findViewById(R.id.lectureTitleText);
        note = findViewById(R.id.lectureTextview);

        title.setText(getIntent().getStringExtra("lecTitle"));
        note.setText(getIntent().getStringExtra("lecNote"));
    }
}

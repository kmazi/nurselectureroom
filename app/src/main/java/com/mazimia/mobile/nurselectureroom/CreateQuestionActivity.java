package com.mazimia.mobile.nurselectureroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateQuestionActivity extends AppCompatActivity {

    public RadioGroup queTypeGroup;
    public RadioGroup ansRadioGroup;
    public RadioButton queRBtn;
    public RadioButton objRBtn;
    public RadioButton theoryRBtn;
    public androidx.gridlayout.widget.GridLayout objGrid;
    public Button createBtn;

    public TextView questionTxt;
    public EditText optATxt;
    public EditText optBTxt;
    public EditText optCText;
    public EditText optDText;

    public FireStoreUtil storeUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        queTypeGroup = findViewById(R.id.queTypeRadioGroup);
        ansRadioGroup = findViewById(R.id.ansRadioGroup);
        final int objId = R.id.objRadioBtn;
        final int theoryId = R.id.theoryRadioBtn;
        final int a = R.id.radioButtonA;
        final int b = R.id.radioButtonB;
        final int c = R.id.radioButtonC;
        final int d = R.id.radioButtonD;

        objRBtn = findViewById(objId);
        theoryRBtn = findViewById(theoryId);
        objGrid = findViewById(R.id.objgridLayout);
        createBtn = findViewById(R.id.submitQue);
        questionTxt = findViewById(R.id.queTxt);
        optATxt = findViewById(R.id.optATxt);
        optBTxt = findViewById(R.id.optBTxt);
        optCText = findViewById(R.id.optCText);
        optDText = findViewById(R.id.optDText);

        storeUtil = new FireStoreUtil(FirebaseFirestore.getInstance());

        // Fill up views if in edit mode
        final Boolean isEdit = getIntent().getBooleanExtra("isEdit", false);
        if (isEdit) {
            questionTxt.setText(getIntent().getStringExtra("question"));
            if (getIntent().getStringExtra("questionType").equals(Question.OBJ)) {
                optATxt.setText(getIntent().getStringExtra("optionA"));
                optBTxt.setText(getIntent().getStringExtra("optionB"));
                optCText.setText(getIntent().getStringExtra("optionC"));
                optDText.setText(getIntent().getStringExtra("optionD"));

                String ans = getIntent().getStringExtra("answer");
                switch (ans) {
                    case "A":
                        ansRadioGroup.check(a);
                        break;

                    case "B":
                        ansRadioGroup.check(b);
                        break;

                    case "C":
                        ansRadioGroup.check(c);
                        break;

                    case "D":
                        ansRadioGroup.check(d);
                        break;
                }
            } else
                queTypeGroup.check(theoryId);
        }


        // Hide obj form if theory question is to be created
        int id = queTypeGroup.getCheckedRadioButtonId();
        queRBtn = findViewById(id);
        if (id == theoryId) {
            objGrid.setVisibility(View.GONE);
        }

        // Fires when the obj or theory check changes
        queTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == objId) {
                    objGrid.setVisibility(View.VISIBLE);
                } else if (checkedId == theoryId) {
                    objGrid.setVisibility(View.GONE);
                }
            }
        });

        // Fires when the user creates submits a question
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question question = new Question();
                int checkedId = queTypeGroup.getCheckedRadioButtonId();
                question.setQuestion(questionTxt.getText().toString());
                question.setSectionId(getIntent().getStringExtra("sectionId"));
                question.setCreatedAt(Calendar.getInstance().getTime().toString());

                OnSuccessListener<Void> theoryComplete = new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CreateQuestionActivity.this,
                                "Successfully created a question", Toast.LENGTH_LONG).show();
                        clearText();
                    }
                };

                OnFailureListener theoryFailure = new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // do something when doc creation fails.
                        Toast.makeText(CreateQuestionActivity.this,
                                "Couldn't create your question; check your internet",
                                Toast.LENGTH_LONG).show();
                    }
                };

                if (checkedId == theoryId) {

                    question.setType(Question.THEO);
                    // Create theory question
                    if (!question.getQuestion().equals(""))
                        if (isEdit) {
                            question.setId(getIntent().getStringExtra("questionId"));
                            storeUtil.updateQuestion(question, theoryComplete, theoryFailure);
                        }
                        else
                            storeUtil.createQuestion(question, theoryComplete, theoryFailure);
                } else if (checkedId == objId) {

                    question.setType(Question.OBJ);
                    // Create obj question
                    Map<String, String> options = new HashMap<>();
                    options.put("A", optATxt.getText().toString());
                    options.put("B", optBTxt.getText().toString());
                    options.put("C", optCText.getText().toString());
                    options.put("D", optDText.getText().toString());
                    question.setOptions(options);

                    int ans = ansRadioGroup.getCheckedRadioButtonId();
                    switch (ans) {
                        case (a):
                            question.setAnswer("A");
                            break;

                        case (b):
                            question.setAnswer("B");
                            break;

                        case (c):
                            question.setAnswer("C");
                            break;

                        case (d):
                            question.setAnswer("D");
                            break;

                    }
                    if(!question.getQuestion().equals("") && !question.getAnswer().equals(""))
                        if (isEdit) {
                            question.setId(getIntent().getStringExtra("questionId"));
                            storeUtil.updateQuestion(question, theoryComplete, theoryFailure);
                        }
                        else
                        storeUtil.createQuestion(question, theoryComplete, theoryFailure);
                }

            }
        });

    }

    public void clearText() {
        questionTxt.setText("");
        optATxt.setText("");
        optBTxt.setText("");
        optCText.setText("");
        optDText.setText("");
    }

}

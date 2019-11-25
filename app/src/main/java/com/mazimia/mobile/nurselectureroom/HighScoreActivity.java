package com.mazimia.mobile.nurselectureroom;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HighScoreActivity extends AppCompatActivity {

    FireStoreUtil storeUtil;
    HighScoreAdapter scoreAdapter;
    ViewHolderClickListener listener;
    private CustomRecyclerView scoreView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        scoreView = findViewById(R.id.highScoreRecyclerview);

        listener = new ViewHolderClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        };
        scoreAdapter = new HighScoreAdapter(listener);
        LinearLayoutManager listManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);
        scoreView.setLayoutManager(listManager);
        scoreView.setAdapter(scoreAdapter);

        storeUtil = new FireStoreUtil(FirebaseFirestore.getInstance());
        OnSuccessListener<QuerySnapshot> success = new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<HighScore> scores = new ArrayList<>();
                for(HighScore score : queryDocumentSnapshots.toObjects(HighScore.class)) {
                    scores.add(score);
                }
                scoreAdapter.setScores(scores);
                Toast.makeText(HighScoreActivity.this, "Successfully loaded high scores",
                        Toast.LENGTH_LONG).show();
            }
        };

        OnFailureListener failure = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HighScoreActivity.this,
                        "Could not get highest scores", Toast.LENGTH_LONG).show();
            }
        };
        storeUtil.getHighestScores(success, failure);
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

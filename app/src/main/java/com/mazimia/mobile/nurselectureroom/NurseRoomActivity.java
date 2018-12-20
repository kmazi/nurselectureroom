package com.mazimia.mobile.nurselectureroom;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NurseRoomActivity extends SignOutActivity {

    private RecyclerView sectionView;
    private SectionAdapter sectionAdapter;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse_room);

        sectionView = findViewById(R.id.sections_recycler_view);
        progress = findViewById(R.id.progressBar2);
        progress.setVisibility(View.VISIBLE);

        LinearLayoutManager listManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        sectionView.setLayoutManager(listManager);
        sectionAdapter = new SectionAdapter();
        sectionView.setAdapter(sectionAdapter);
        FireStoreUtil storeUtil = new FireStoreUtil(FirebaseFirestore.getInstance());

        OnCompleteListener successListener = new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    final ArrayList<Section> sections = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        sections.add(document.toObject(Section.class));
                    }

                    progress.setVisibility(View.GONE);
                    sectionAdapter.setSection(sections);

                } else {

                    // do something if it fails
                    Toast.makeText(NurseRoomActivity.this, "Unable to load sections",
                            Toast.LENGTH_LONG);
                }
            }
        };

        storeUtil.getLectureSections(successListener);

//        loadSections();
    }

    private void loadSections() {

        new FetchSectionTask().execute();
    }

    public class FetchSectionTask extends AsyncTask<Void, Void, ArrayList<Section>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Section> doInBackground(Void... section) {

            FireStoreUtil storeUtil = new FireStoreUtil(FirebaseFirestore.getInstance());
            final ArrayList<Section> sections = new ArrayList<>();
            try{
                OnCompleteListener successListener = new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                sections.add(document.toObject(Section.class));
                            }

                        } else {

                            // do something if it fails
                        }
                    }
                };

                storeUtil.getLectureSections(successListener);
                return sections;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Section> sections) {
            super.onPostExecute(sections);
            sectionAdapter.setSection(sections);
        }
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

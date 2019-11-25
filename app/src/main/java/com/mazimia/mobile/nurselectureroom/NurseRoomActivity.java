package com.mazimia.mobile.nurselectureroom;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageButton;
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

    private CustomRecyclerView sectionView;
    private SectionAdapter sectionAdapter;
    private ProgressBar progress;
    private ViewHolderClickListener sectionClickListener;
    private ViewHolderClickListener optionListener;
    private FloatingActionButton createSectionBtn;
    private FloatingActionButton refreshButton;
    private ImageButton optionBtn;
    FireStoreUtil storeUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse_room);

        sectionView = findViewById(R.id.sections_recycler_view);
        sectionView.setHasFixedSize(true);

        progress = findViewById(R.id.progressBar2);
        progress.setVisibility(View.VISIBLE);
        createSectionBtn = findViewById(R.id.addSectionBtn);
        String adminMail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (adminMail.equals("nurse.lectureroom@gmail.com") || adminMail.equals("kingsleyu13@yahoo.com"))
            createSectionBtn.setVisibility(View.VISIBLE);
        else
            createSectionBtn.setVisibility(View.GONE);
        refreshButton = findViewById(R.id.refreshFoatBtn);

        // Fires when the section float button is clicked
        createSectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NurseRoomActivity.this,
                        CreateActivity.class);
                intent.putExtra("isCreate", "section");
                startActivity(intent);
            }
        });


        LinearLayoutManager listManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);
        sectionView.setLayoutManager(listManager);

        // Set onclick listener for every list item when viewing sections
        sectionClickListener = new ViewHolderClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent lectureIntent = new Intent(NurseRoomActivity.this,
                        LectureListActivity.class);
                String sectionId = sectionAdapter.getSections().get(position).getId();
                lectureIntent.putExtra("sectionId", sectionId);
                startActivity(lectureIntent);
            }
        };

        optionListener = new ViewHolderClickListener() {
            @Override
            public void onClick(View view, int position) {
                // register for context menu
                if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("nurse.lectureroom@gmail.com")){
//                    view.setLongClickable(false);
                    registerForContextMenu(view);
                }

            }
        };

        sectionAdapter = new SectionAdapter(sectionClickListener);
        sectionAdapter.setContext(this);
        sectionAdapter.setOptionListener(optionListener);
        sectionView.setAdapter(sectionAdapter);
        storeUtil = new FireStoreUtil(FirebaseFirestore.getInstance());

        //Fires when the refresh button is clicked
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeUtil.getLectureSections(loadSectionsListener());
                refreshButton.setVisibility(View.GONE);
            }
        });

        storeUtil.getLectureSections(loadSectionsListener());
    }

    public OnCompleteListener<QuerySnapshot> loadSectionsListener() {
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
                    progress.setVisibility(View.GONE);
                    Toast.makeText(NurseRoomActivity.this, "Unable to load sections",
                            Toast.LENGTH_LONG);
                }
            }
        };
        return successListener;
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

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshButton.setVisibility(View.VISIBLE);
    }

}

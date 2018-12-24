package com.mazimia.mobile.nurselectureroom;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
    private FloatingActionButton createSectionBtn;
    private FloatingActionButton refreshButton;
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

        // register for context menu
        registerForContextMenu(sectionView);

        sectionAdapter = new SectionAdapter(sectionClickListener);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        // inflate menu from xml
        MenuInflater inflater = NurseRoomActivity.this.getMenuInflater();
        inflater.inflate(R.menu.section_options, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Get position info
        CustomRecyclerView.RecyclerContextMenuInfo itemInfo =
                (CustomRecyclerView.RecyclerContextMenuInfo) item.getMenuInfo();
        final Section section = sectionAdapter.getSections().get(itemInfo.position);
        Intent intent = new Intent(this, CreateActivity.class);

        switch (item.getItemId()) {
            // Create a lecture from a section.
            case (R.id.create_lecture_menu):
                intent.putExtra("sectionId", section.getId());
                intent.putExtra("isCreate", "lecture");
                startActivity(intent);
                return true;

                // Edit a section
            case (R.id.edit_section_menu):
                intent.putExtra("isCreate", "section");
                intent.putExtra("isEdit", true);
                intent.putExtra("title", section.getTitle());
                intent.putExtra("summary", section.getSummary());
                intent.putExtra("id", section.getId());
                startActivity(intent);
                return true;

                // Delete a section
            case (R.id.delete_section_menu):
                final FireStoreUtil storeUtil = new FireStoreUtil(FirebaseFirestore.getInstance());
                final OnCompleteListener<Void> success = new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // do something when its successful
                        Toast.makeText(NurseRoomActivity.this,
                                "Successfully deleted the section and it's lectures",
                                Toast.LENGTH_SHORT).show();
                    }
                };
                final OnFailureListener failure = new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // do something when it fails.
                        Toast.makeText(NurseRoomActivity.this,
                                "Could not delete the section nor it's lectures," +
                                        " check your internet connection",
                                Toast.LENGTH_SHORT).show();
                    }
                };

                AlertDialog.Builder alert = new AlertDialog.Builder(NurseRoomActivity.this);
                alert.setTitle("Delete Operation");
                alert.setMessage("Do you rely want to delete this section? Note that ALL LECTURES " +
                        "in this section would be deleted");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        storeUtil.deleteSection(section.getId(), success, failure);
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();

                return true;
        }
        return true;
    }
}

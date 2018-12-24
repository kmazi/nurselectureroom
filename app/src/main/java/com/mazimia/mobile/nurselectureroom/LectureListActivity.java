package com.mazimia.mobile.nurselectureroom;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LectureListActivity extends AppCompatActivity {

    private CustomRecyclerView lectureView;

    private ProgressBar progress;
    private ViewHolderClickListener lectureClickListener;
    FireStoreUtil storeUtil;
    LectureAdapter lectureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_list);

        lectureView = findViewById(R.id.lecture_recycle_view);

        progress = findViewById(R.id.progressBar);
        progress.setVisibility(View.VISIBLE);

        LinearLayoutManager listManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        lectureView.setLayoutManager(listManager);
        registerForContextMenu(lectureView);

        // Set onclick listener for every list item when viewing sections
        lectureClickListener = new ViewHolderClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent lectureIntent = new Intent(LectureListActivity.this, LectureContentActivity.class);
                String topic = lectureAdapter.getLecture(position).getTopic();
                String note = lectureAdapter.getLecture(position).getNote();
                lectureIntent.putExtra("lecTitle", topic);
                lectureIntent.putExtra("lecNote", note);
                startActivity(lectureIntent);
            }
        };

        lectureAdapter = new LectureAdapter(lectureClickListener);
        lectureView.setAdapter(lectureAdapter);

        storeUtil = new FireStoreUtil(FirebaseFirestore.getInstance());
        String sectionId = getIntent().getStringExtra("sectionId");



        storeUtil.getLectures(sectionId, success, failure);
    }

    OnSuccessListener<QuerySnapshot> success = new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            ArrayList<Lecture> lectures = new ArrayList<>();
            for (Lecture lecture : queryDocumentSnapshots.toObjects(Lecture.class)) {

                lectures.add(lecture);
            }
            lectureAdapter.setLectures(lectures);
            progress.setVisibility(View.GONE);

        }
    };

    OnFailureListener failure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(LectureListActivity.this, "Unable to fetch lectures, check your internet",
                    Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = LectureListActivity.this.getMenuInflater();
        inflater.inflate(R.menu.lecture_options, menu);
    }

    // Fires when a lecture is deleted
    OnSuccessListener<Void> delSuccess = new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            // Notify user that delete operation was successful
            Toast.makeText(LectureListActivity.this, "Lecture was successfully deleted!",
                    Toast.LENGTH_LONG).show();
        }
    };

    OnFailureListener delFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {

            // Notify user that delete wasn't successful
            Toast.makeText(LectureListActivity.this, "Lecture could not be deleted! Check your internet connection",
                    Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Get position info
        CustomRecyclerView.RecyclerContextMenuInfo itemInfo =
                (CustomRecyclerView.RecyclerContextMenuInfo) item.getMenuInfo();

        final Lecture lecture = lectureAdapter.getLectures().get(itemInfo.position);
        Intent intent = new Intent(this, CreateActivity.class);

        switch (item.getItemId()) {
            case (R.id.edit_lecture_menu):
                intent.putExtra("isCreate", "lecture");
                intent.putExtra("isEdit", true);
                intent.putExtra("title", lecture.getTopic());
                intent.putExtra("summary", lecture.getDescription());
                intent.putExtra("id", lecture.getId());
                startActivity(intent);
                return true;

            case (R.id.delete_lecture_menu):
                final FireStoreUtil storeUtil = new FireStoreUtil(FirebaseFirestore.getInstance());
                AlertDialog.Builder alert = new AlertDialog.Builder(LectureListActivity.this);
                alert.setTitle("Delete Operation");
                alert.setMessage("Do you rely want to delete this lecture?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        storeUtil.deleteLecture(lecture.getId(), delSuccess, delFailure);
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

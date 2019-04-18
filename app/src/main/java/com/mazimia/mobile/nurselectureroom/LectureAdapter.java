package com.mazimia.mobile.nurselectureroom;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class LectureAdapter extends CustomBaseAdapter {

    private ViewHolderClickListener listener;
    private Context context;
    private ArrayList<Lecture> lectures = new ArrayList<>();


    public LectureAdapter(ViewHolderClickListener listener) {
        this.listener = listener;
    }

    public void setLectures(ArrayList<Lecture> lectures) {
        this.lectures.clear();
        this.lectures.addAll(lectures);
        notifyDataSetChanged();
    }

    public Lecture getLecture(int position) {
        return lectures.get(position);
    }

    public ArrayList<Lecture> getLectures() {
        return this.lectures;
    }

    @NonNull
    @Override
    public CustomBaseAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        int sectionListLayoutId = R.layout.section_listview_item;
        View view = inflater.inflate(sectionListLayoutId, parent, false);
        return new CustomBaseAdapterViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomBaseAdapterViewHolder holder, int position) {
        final Lecture lecture = lectures.get(position);
        holder.sectionTitle.setText(lecture.getTopic());
        holder.sectionDesc.setText(lecture.getDescription());
        //holder.itemView.setLongClickable(true);
        holder.optionImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int CONTEXT_EDIT_LECTURE = 1;
                final int CONTEXT_DELETE_LECTURE = 2;
                PopupMenu menu = new PopupMenu(context, holder.optionImgBtn);
                String adminMails = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                if (adminMails.equals("nurse.lectureroom@gmail.com") || adminMails.equals("kingsleyu13@yahoo.com")){
                    menu.getMenu().add(Menu.NONE, CONTEXT_EDIT_LECTURE, 1, "Edit Lecture");
                    menu.getMenu().add(Menu.NONE, CONTEXT_DELETE_LECTURE, 2, "Delete Lecture");
                }
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = new Intent(context, CreateActivity.class);

                        // Fires when a lecture is deleted
                        final OnSuccessListener<Void> delSuccess = new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Notify user that delete operation was successful
                                Toast.makeText(context, "Lecture was successfully deleted!",
                                        Toast.LENGTH_LONG).show();
                            }
                        };

                        final OnFailureListener delFailure = new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                // Notify user that delete wasn't successful
                                Toast.makeText(context, "Lecture could not be deleted! Check your internet connection",
                                        Toast.LENGTH_LONG).show();
                            }
                        };
                        switch (item.getItemId()) {
                            case CONTEXT_EDIT_LECTURE:
                                intent.putExtra("isCreate", "lecture");
                                intent.putExtra("isEdit", true);
                                intent.putExtra("title", lecture.getTopic());
                                intent.putExtra("summary", lecture.getDescription());
                                intent.putExtra("note", lecture.getNote());
                                intent.putExtra("lectureId", lecture.getId());
                                context.startActivity(intent);
                                return true;

                            case CONTEXT_DELETE_LECTURE:
                                final FireStoreUtil storeUtil = new FireStoreUtil(FirebaseFirestore.getInstance());
                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
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

                            default:
                                return true;
                        }
                    }
                });
                menu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (lectures == null) {
            return 0;
        }
        return lectures.size();
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

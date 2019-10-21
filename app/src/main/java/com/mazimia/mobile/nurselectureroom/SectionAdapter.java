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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
//import org.apache.commons.lang3.StringUtils;

class SectionAdapter extends CustomBaseAdapter {

    private ArrayList<Section> sections = new ArrayList<>();
    private ViewHolderClickListener listener;
    private ViewHolderClickListener optionListener;
    private Context context;


    public SectionAdapter(ViewHolderClickListener listener) {
        this.listener = listener;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void setSection(ArrayList<Section> sections) {
        this.sections.clear();
        this.sections.addAll(sections);
        notifyDataSetChanged();
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
    public void onBindViewHolder(@NonNull final CustomBaseAdapterViewHolder holder, final int position) {
            final Section section = sections.get(position);
            holder.sectionTitle.setText(section.getTitle());
            holder.sectionDesc.setText(section.getSummary());
            holder.optionImgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                    final int CONTEXT_CREATE_LECTURE = 1;
                    final int CONTEXT_CREATE_QUESTION = 2;
                    final int CONTEXT_EDIT_SECTION = 3;
                    final int CONTEXT_DELETE_SECTION = 4;
                    final int CONTEXT_OPEN_QUESTIONS = 5;
                    PopupMenu menu = new PopupMenu(context, holder.optionImgBtn);
                    String adminMails = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    if (adminMails.equals("nurse.lectureroom@gmail.com") || adminMails.equals("kingsleyu13@yahoo.com")){
                        menu.getMenu().add(Menu.NONE, CONTEXT_CREATE_LECTURE, 1, "Create Lecture");
                        menu.getMenu().add(Menu.NONE, CONTEXT_CREATE_QUESTION, 2, "Create Question");
                        menu.getMenu().add(Menu.NONE, CONTEXT_EDIT_SECTION, 3, "Edit Section");
                        menu.getMenu().add(Menu.NONE, CONTEXT_DELETE_SECTION, 4, "Delete Section");
                    }
                    menu.getMenu().add(Menu.NONE, CONTEXT_OPEN_QUESTIONS, 5, "View Questions");
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent intent = new Intent(context, CreateActivity.class);
                            switch (item.getItemId()) {
                                case CONTEXT_OPEN_QUESTIONS:
                                    Intent lectureIntent = new Intent(context, LectureContentActivity.class);
                                    lectureIntent.putExtra("sectionId", section.getId());
                                    context.startActivity(lectureIntent);
                                    return true;

                                case CONTEXT_DELETE_SECTION:
                                    final FireStoreUtil storeUtil = new FireStoreUtil(FirebaseFirestore.getInstance());
                                    final OnCompleteListener<Void> success = new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // do something when its successful
                                            Toast.makeText(context,
                                                    "Successfully deleted the section and it's lectures",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    };
                                    final OnFailureListener failure = new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // do something when it fails.
                                            Toast.makeText(context,
                                                    "Could not delete the section nor it's lectures," +
                                                            " check your internet connection",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    };

                                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
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

                                case CONTEXT_EDIT_SECTION:
                                    intent.putExtra("isCreate", "section");
                                    intent.putExtra("isEdit", true);
                                    intent.putExtra("title", section.getTitle());
                                    intent.putExtra("summary", section.getSummary());
                                    intent.putExtra("sectionId", section.getId());
                                    context.startActivity(intent);
                                    return true;

                                case CONTEXT_CREATE_QUESTION:
                                    Intent queIntent = new Intent(context, CreateQuestionActivity.class);
                                    queIntent.putExtra("sectionId", section.getId());
                                    context.startActivity(queIntent);
                                    return true;

                                case CONTEXT_CREATE_LECTURE:
                                    intent.putExtra("sectionId", section.getId());
                                    intent.putExtra("isCreate", "lecture");
                                    context.startActivity(intent);
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
        if (sections == null) {
            return 0;
        }
        return sections.size();
    }

    public void setOptionListener(ViewHolderClickListener optionListener) {
        this.optionListener = optionListener;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

package com.mazimia.mobile.nurselectureroom;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class LectureAdapter extends CustomBaseAdapter {

    private ViewHolderClickListener listener;
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
    public void onBindViewHolder(@NonNull CustomBaseAdapterViewHolder holder, int position) {
        Lecture lecture = lectures.get(position);
        holder.sectionTitle.setText(lecture.getTopic());
        holder.sectionDesc.setText(lecture.getDescription());
        holder.itemView.setLongClickable(true);
    }

    @Override
    public int getItemCount() {
        if (lectures == null) {
            return 0;
        }
        return lectures.size();
    }
}

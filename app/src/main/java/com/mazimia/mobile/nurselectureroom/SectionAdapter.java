package com.mazimia.mobile.nurselectureroom;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

class SectionAdapter extends CustomBaseAdapter {

    private ArrayList<Section> sections = new ArrayList<>();
    private ViewHolderClickListener listener;


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
    public void onBindViewHolder(@NonNull CustomBaseAdapterViewHolder holder, int position) {
            Section section = sections.get(position);
            holder.sectionTitle.setText(section.getTitle());
            holder.sectionDesc.setText(section.getSummary());
            holder.itemView.setLongClickable(true);

    }

    @Override
    public int getItemCount() {
        if (sections == null) {
            return 0;
        }
        return sections.size();
    }
}

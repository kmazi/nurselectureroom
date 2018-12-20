package com.mazimia.mobile.nurselectureroom;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionAdapterViewHolder> {

    public SectionAdapter() {

    }

    public ArrayList<Section> getSection() {
        return sections;
    }

    public void setSection(ArrayList<Section> sections) {
        this.sections = sections;
        notifyDataSetChanged();
    }

    private ArrayList<Section> sections;


    public class SectionAdapterViewHolder extends RecyclerView.ViewHolder {

        public TextView sectionTitle;
        public TextView sectionDesc;

        public SectionAdapterViewHolder (View view) {
            super(view);
            sectionTitle = view.findViewById(R.id.section_title_txtview);
            sectionDesc = view.findViewById(R.id.section_desc_txtview);
        }
    }

    @NonNull
    @Override
    public SectionAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        int sectionListLayoutId = R.layout.section_listview_item;
        View view = inflater.inflate(sectionListLayoutId, parent, false);
        return new SectionAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionAdapterViewHolder holder, int position) {
        Section section = sections.get(position);
        holder.sectionTitle.setText(section.getTitle());
        holder.sectionDesc.setText(section.getSummary());
    }

    @Override
    public int getItemCount() {
        if (sections == null) {
            return 0;
        }
        return sections.size();
    }
}

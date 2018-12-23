package com.mazimia.mobile.nurselectureroom;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

class SectionAdapter extends CustomRecyclerView.Adapter<SectionAdapter.SectionAdapterViewHolder> {

    private ArrayList<Section> sections;
    private ViewHolderClickListener listener;

    public SectionAdapter() {

    }

    public SectionAdapter(ViewHolderClickListener listener) {
        this.listener = listener;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void setSection(ArrayList<Section> sections) {
        this.sections = sections;
        notifyDataSetChanged();
    }


    public class SectionAdapterViewHolder extends CustomRecyclerView.ViewHolder
        implements View.OnClickListener{

        public TextView sectionTitle;
        public TextView sectionDesc;
        private ViewHolderClickListener sectionClickListener;

//        public SectionAdapterViewHolder (View view) {
//            super(view);
//            sectionTitle = view.findViewById(R.id.section_title_txtview);
//            sectionDesc = view.findViewById(R.id.section_desc_txtview);
//        }

        public SectionAdapterViewHolder (View view, ViewHolderClickListener listener) {
            super(view);
            sectionTitle = view.findViewById(R.id.section_title_txtview);
            sectionDesc = view.findViewById(R.id.section_desc_txtview);
            sectionClickListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            sectionClickListener.onClick(view, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public SectionAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        int sectionListLayoutId = R.layout.section_listview_item;
        View view = inflater.inflate(sectionListLayoutId, parent, false);
        return new SectionAdapterViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionAdapterViewHolder holder, int position) {
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

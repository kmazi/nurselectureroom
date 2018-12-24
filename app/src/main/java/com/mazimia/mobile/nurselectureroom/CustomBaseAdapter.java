package com.mazimia.mobile.nurselectureroom;

import android.view.View;
import android.widget.TextView;

public abstract class CustomBaseAdapter extends CustomRecyclerView.Adapter<CustomBaseAdapter.CustomBaseAdapterViewHolder> {

    private ViewHolderClickListener listener;

    public class CustomBaseAdapterViewHolder extends CustomRecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView sectionTitle;
        public TextView sectionDesc;
        private ViewHolderClickListener clickListener;

        public CustomBaseAdapterViewHolder(View view, ViewHolderClickListener listener) {
            super(view);
            sectionTitle = view.findViewById(R.id.section_title_txtview);
            sectionDesc = view.findViewById(R.id.section_desc_txtview);
            clickListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition());
        }
    }

}
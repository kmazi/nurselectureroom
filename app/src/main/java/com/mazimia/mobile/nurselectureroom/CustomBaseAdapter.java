package com.mazimia.mobile.nurselectureroom;

import android.view.View;
import android.widget.TextView;

public abstract class CustomBaseAdapter extends CustomRecyclerView.Adapter<CustomBaseAdapter.CustomBaseAdapterViewHolder> {

    public class CustomBaseAdapterViewHolder extends CustomRecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView sectionTitle;
        public TextView sectionDesc;

        public TextView questionNo;
        public TextView question;
        private ViewHolderClickListener clickListener;

        public CustomBaseAdapterViewHolder(View view, ViewHolderClickListener listener) {
            super(view);
            sectionTitle = view.findViewById(R.id.section_title_txtview);
            sectionDesc = view.findViewById(R.id.section_desc_txtview);
            clickListener = listener;
            view.setOnClickListener(this);
        }

        // Constructor to initialize the text fields in the question lists
        public CustomBaseAdapterViewHolder(View view) {
            super(view);
            questionNo = view.findViewById(R.id.quetxtno);
            question = view.findViewById(R.id.quetxt);
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition());
        }
    }

}
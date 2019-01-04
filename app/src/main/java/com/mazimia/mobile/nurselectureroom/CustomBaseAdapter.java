package com.mazimia.mobile.nurselectureroom;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public abstract class CustomBaseAdapter extends CustomRecyclerView.Adapter<CustomBaseAdapter.CustomBaseAdapterViewHolder> {

    public class CustomBaseAdapterViewHolder extends CustomRecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView sectionTitle;
        public TextView sectionDesc;

        public TextView questionNo;
        public TextView question;

        public TextView objQue;
        public TextView objQueCount;
        public CheckBox optA;
        public CheckBox optB;
        public CheckBox optC;
        public CheckBox optD;

        public ViewHolderClickListener clickListener;

        public CustomBaseAdapterViewHolder(View view, ViewHolderClickListener listener) {
            super(view);
            sectionTitle = view.findViewById(R.id.section_title_txtview);
            sectionDesc = view.findViewById(R.id.section_desc_txtview);
            clickListener = listener;
            view.setOnClickListener(this);
        }

        // Constructor to initialize the text fields in the question lists
        public CustomBaseAdapterViewHolder(View view, ViewHolderClickListener listener, String queType) {
            super(view);
            switch (queType) {
                case (Question.THEO):
                    questionNo = view.findViewById(R.id.quetxtno);
                    question = view.findViewById(R.id.quetxt);
                    break;

                case (Question.OBJ):
                    objQue = view.findViewById(R.id.objQuetxt);
                    objQueCount = view.findViewById(R.id.objQueCount);
                    optA = view.findViewById(R.id.checkBox);
                    optB = view.findViewById(R.id.checkBox2);
                    optC = view.findViewById(R.id.checkBox3);
                    optD = view.findViewById(R.id.checkBox4);

            }
            clickListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition());
        }

    }

}
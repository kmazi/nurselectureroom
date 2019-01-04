package com.mazimia.mobile.nurselectureroom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;


public class ObjectiveQueAdapter extends CustomBaseAdapter {

    private Context mContext;

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    interface Change {
        void changeBkgColor(CheckBox selectBox);
        void disable();
    }

    public  ObjectiveQueAdapter(Context context) {
        mContext = context;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions.clear();
        this.questions.addAll(questions);
        notifyDataSetChanged();
    }

    public void setListener(ViewHolderClickListener listener) {
        this.listener = listener;
    }

    private ViewHolderClickListener listener;

    private ArrayList<Question> questions = new ArrayList<>();

    @NonNull
    @Override
    public CustomBaseAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.obj_question_list, parent, false);
        CustomBaseAdapterViewHolder adapter = new CustomBaseAdapterViewHolder(view, listener, Question.OBJ);
        return adapter;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomBaseAdapterViewHolder holder, int position) {
        Question question = getQuestions().get(position);
        holder.objQue.setText(question.getQuestion());
        holder.objQueCount.setText(String.valueOf(position+1));


        final CheckBox checkBox = holder.optA;
        final CheckBox checkBox2 = holder.optB;
        final CheckBox checkBox3 = holder.optC;
        final CheckBox checkBox4 = holder.optD;

        holder.optA.setText(question.getOptions().get("A"));
        holder.optB.setText(question.getOptions().get("B"));
        holder.optC.setText(question.getOptions().get("C"));
        holder.optD.setText(question.getOptions().get("D"));

        final String ans = question.getOptions().get(question.getAnswer());
        final Change change = new Change() {
            @Override
            public void changeBkgColor(CheckBox selectedBox) {
                CheckBox[] checkBoxes = {checkBox, checkBox2, checkBox3, checkBox4};
                CheckBox ansBox = new CheckBox(mContext);

                // Get the answer checkbox
                for (CheckBox check : checkBoxes) {
                    if (check.getText().toString().equals(ans)) {
                        ansBox = check;
                        break;
                    }
                }
                if (selectedBox == ansBox) {
                    selectedBox.setBackgroundColor(mContext.getResources().getColor(R.color.fui_bgPhone));
                    selectedBox.setTextColor(mContext.getResources().getColor(R.color.browser_actions_bg_grey));

                } else {
                    selectedBox.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                    selectedBox.setTextColor(mContext.getResources().getColor(R.color.browser_actions_bg_grey));
                    ansBox.setBackgroundColor(mContext.getResources().getColor(R.color.fui_bgPhone));
                    ansBox.setTextColor(mContext.getResources().getColor(R.color.browser_actions_bg_grey));

                }
            }

            @Override
            public void disable() {
                checkBox.setEnabled(false);
                checkBox2.setEnabled(false);
                checkBox3.setEnabled(false);
                checkBox4.setEnabled(false);
            }
        };

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change.disable();
                change.changeBkgColor(checkBox);
            }
        });

        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change.disable();
                change.changeBkgColor(checkBox2);
            }
        });

        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change.disable();
                change.changeBkgColor(checkBox3);
            }
        });

        checkBox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change.disable();
                change.changeBkgColor(checkBox4);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (getQuestions() == null){
            return 0;
        }
        return getQuestions().size();
    }


}

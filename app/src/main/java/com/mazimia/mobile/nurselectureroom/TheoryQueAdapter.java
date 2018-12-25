package com.mazimia.mobile.nurselectureroom;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class TheoryQueAdapter extends CustomBaseAdapter {

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions.clear();
        this.questions.addAll(questions);
        notifyDataSetChanged();
    }

    public ArrayList<Question> questions = new ArrayList<>();


    @NonNull
    @Override
    public CustomBaseAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        int question_list_layout_id = R.layout.theory_question_list;
        View view = inflater.inflate(question_list_layout_id, parent, false);
        return new CustomBaseAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomBaseAdapterViewHolder holder, int position) {
        Question question = getQuestions().get(position);
        holder.questionNo.setText(String.valueOf(position));
        holder.question.setText(question.getQuestion());
    }

    @Override
    public int getItemCount() {
        if (this.getQuestions() == null){
            return 0;
        }
        return this.getQuestions().size();
    }
}

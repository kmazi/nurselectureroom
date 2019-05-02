package com.mazimia.mobile.nurselectureroom;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class TheoryQueAdapter extends CustomBaseAdapter {

//    private Activity activity;

    public  TheoryQueAdapter()
    {

    }

//    public TheoryQueAdapter(Activity activity) {
//        this.activity = activity;
//    }
    public ArrayList<Question> getQuestions() {
        return questions;
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

    public void setImgButtonListener(View.OnClickListener imgButtonListener) {
        this.imgButtonListener = imgButtonListener;
    }

    private View.OnClickListener imgButtonListener;
    public ArrayList<Question> questions = new ArrayList<>();


    @NonNull
    @Override
    public CustomBaseAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        int question_list_layout_id = R.layout.theory_question_list;
        View view = inflater.inflate(question_list_layout_id, parent, false);
        return new CustomBaseAdapterViewHolder(view, listener, Question.THEO);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomBaseAdapterViewHolder holder, int position) {
        Question question = getQuestions().get(position);
        holder.questionNo.setText(String.valueOf(position+1));
        holder.question.setText(question.getQuestion());
        holder.itemView.setLongClickable(true);
    }

    @Override
    public int getItemCount() {
        if (getQuestions() == null){
            return 0;
        }
        return getQuestions().size();
    }
}

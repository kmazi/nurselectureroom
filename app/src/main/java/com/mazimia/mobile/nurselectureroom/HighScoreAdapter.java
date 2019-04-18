package com.mazimia.mobile.nurselectureroom;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class HighScoreAdapter extends CustomBaseAdapter {

    private ViewHolderClickListener listener;

    public ArrayList<HighScore> getScores() {
        return scores;
    }

    public void setScores(ArrayList<HighScore> scores) {
        this.scores.clear();
        this.scores.addAll(scores);
        notifyDataSetChanged();
    }

    private ArrayList<HighScore> scores = new ArrayList<>();

    public HighScoreAdapter(ViewHolderClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public CustomBaseAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        int sectionListLayoutId = R.layout.highest_score_list;
        View view = inflater.inflate(sectionListLayoutId, parent, false);
        return new CustomBaseAdapterViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomBaseAdapterViewHolder holder, int position) {
        HighScore score = scores.get(position);
        holder.scoreSection.setText(score.getScoreSection());
        holder.scoreNames.setText(score.getScorer());
        holder.scorePoints.setText(String.valueOf(score.getHighestPoint()));
    }

    @Override
    public int getItemCount() {
        if (scores == null) {
            return 0;
        }
        return scores.size();
    }
}

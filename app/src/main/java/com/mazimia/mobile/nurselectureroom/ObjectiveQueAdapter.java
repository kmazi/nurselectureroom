package com.mazimia.mobile.nurselectureroom;

import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ObjectiveQueAdapter extends CustomBaseAdapter {

    private Context mContext;
    private String objectiveSection;

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    ArrayList<Map<String, Boolean>> checkStates = new ArrayList<>();
    int correctAnswer = 0;
    int totalQuestion = 0;

    public void setObjectiveSection(String objectiveSection) {
        this.objectiveSection = objectiveSection;
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
        totalQuestion = this.questions.size();
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
    public void onBindViewHolder(@NonNull CustomBaseAdapterViewHolder holder, final int position) {
        Question question = getQuestions().get(position);
        holder.objQue.setText(question.getQuestion());
        holder.objQueCount.setText(String.valueOf(position+1));
        holder.itemView.setLongClickable(true);


        final CheckBox checkBox = holder.optA;
        final CheckBox checkBox2 = holder.optB;
        final CheckBox checkBox3 = holder.optC;
        final CheckBox checkBox4 = holder.optD;

        if (question.getOptions().get("C").equals("") &&
                question.getOptions().get("D").equals("")) {
            holder.optC.setVisibility(View.GONE);
            holder.optD.setVisibility(View.GONE);
        } else {
            holder.optC.setVisibility(View.VISIBLE);
            holder.optD.setVisibility(View.VISIBLE);
        }

        holder.optA.setText(question.getOptions().get("A"));
        holder.optB.setText(question.getOptions().get("B"));
        holder.optC.setText(question.getOptions().get("C"));
        holder.optD.setText(question.getOptions().get("D"));

        holder.optA.setEnabled(true);
        holder.optA.setChecked(false);
        holder.optA.setBackgroundColor(mContext.getResources().getColor(R.color.backgroundImg));
        holder.optA.setTextColor(mContext.getResources().getColor(R.color.fui_bgGitHub));

        holder.optB.setEnabled(true);
        holder.optB.setChecked(false);
        holder.optB.setBackgroundColor(mContext.getResources().getColor(R.color.backgroundImg));
        holder.optB.setTextColor(mContext.getResources().getColor(R.color.fui_bgGitHub));

        holder.optC.setEnabled(true);
        holder.optC.setChecked(false);
        holder.optC.setBackgroundColor(mContext.getResources().getColor(R.color.backgroundImg));
        holder.optC.setTextColor(mContext.getResources().getColor(R.color.fui_bgGitHub));

        holder.optD.setEnabled(true);
        holder.optD.setChecked(false);
        holder.optD.setBackgroundColor(mContext.getResources().getColor(R.color.backgroundImg));
        holder.optD.setTextColor(mContext.getResources().getColor(R.color.fui_bgGitHub));

        final Map<String, Boolean> state = new HashMap<>();
        checkStates.add(state);
        if (checkStates.size() > 0 && position < checkStates.size()) {
            if (checkStates.get(position).containsKey("answered") &&
                    checkStates.get(position).get("answered")) {
                holder.optA.setEnabled(false);
                holder.optB.setEnabled(false);
                holder.optC.setEnabled(false);
                holder.optD.setEnabled(false);
                for (String key : checkStates.get(position).keySet()) {
                    switch (key) {
                        case "A":
                            if (!key.equals(question.getAnswer())) {
                                holder.optA.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                                holder.optA.setTextColor(mContext.getResources().getColor(R.color.browser_actions_bg_grey));

                            }
                            holder.optA.setChecked(true);
                            break;

                        case "B":
                            if (!key.equals(question.getAnswer())) {
                                holder.optB.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                                holder.optB.setTextColor(mContext.getResources().getColor(R.color.browser_actions_bg_grey));

                            }
                            holder.optB.setChecked(true);
                            break;

                        case "C":
                            if (!key.equals(question.getAnswer())) {
                                holder.optC.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                                holder.optC.setTextColor(mContext.getResources().getColor(R.color.browser_actions_bg_grey));

                            }
                            holder.optC.setChecked(true);
                            break;

                        case "D":
                            if (!key.equals(question.getAnswer())) {
                                holder.optD.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                                holder.optD.setTextColor(mContext.getResources().getColor(R.color.browser_actions_bg_grey));

                            }
                            holder.optD.setChecked(true);
                            break;
                    }
                }
                switch (question.getAnswer()) {
                    case "A":
                        holder.optA.setBackgroundColor(mContext.getResources().getColor(R.color.fui_bgPhone));
                        holder.optA.setTextColor(mContext.getResources().getColor(R.color.browser_actions_bg_grey));
                        break;

                    case "B":
                        holder.optB.setBackgroundColor(mContext.getResources().getColor(R.color.fui_bgPhone));
                        holder.optB.setTextColor(mContext.getResources().getColor(R.color.browser_actions_bg_grey));
                        break;

                    case "C":
                        holder.optC.setBackgroundColor(mContext.getResources().getColor(R.color.fui_bgPhone));
                        holder.optC.setTextColor(mContext.getResources().getColor(R.color.browser_actions_bg_grey));
                        break;

                    case "D":
                        holder.optD.setBackgroundColor(mContext.getResources().getColor(R.color.fui_bgPhone));
                        holder.optD.setTextColor(mContext.getResources().getColor(R.color.browser_actions_bg_grey));
                        break;
                }

            }
        }


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
                    correctAnswer ++;
                    selectedBox.setBackgroundColor(mContext.getResources().getColor(R.color.fui_bgPhone));
                    selectedBox.setTextColor(mContext.getResources().getColor(R.color.browser_actions_bg_grey));

                } else {
                    selectedBox.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                    selectedBox.setTextColor(mContext.getResources().getColor(R.color.browser_actions_bg_grey));
                    ansBox.setBackgroundColor(mContext.getResources().getColor(R.color.fui_bgPhone));
                    ansBox.setTextColor(mContext.getResources().getColor(R.color.browser_actions_bg_grey));

                }
                if (totalQuestion == 0)
                    runScoreReport();
            }

            private void runScoreReport() {
                AlertDialog dialog = new AlertDialog.Builder(mContext).create();
                dialog.setTitle("Score");
                dialog.setMessage("Congratulations on attempting all questions \n" +
                        "Your scored "+ String.valueOf(correctAnswer) + " points");


                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        final OnFailureListener failure = new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(mContext, "Highest score successfully recorded",
                                        Toast.LENGTH_SHORT).show();
                            }
                        };

                        final OnSuccessListener<Void> success = new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialog.dismiss();
                                Toast.makeText(mContext, "Check your internet connection",
                                        Toast.LENGTH_SHORT).show();
                            }
                        };
                        final FireStoreUtil storeUtil = new FireStoreUtil(FirebaseFirestore.getInstance());

                        OnSuccessListener<QuerySnapshot> successful = new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                ArrayList<HighScore> scores = new ArrayList<>();
                                for (HighScore score : queryDocumentSnapshots.toObjects(HighScore.class)) {
                                    scores.add(score);
                                }
                                if (scores.size() == 0 || scores.get(0).getHighestPoint() > correctAnswer) {
                                    HighScore score = new HighScore();
                                    score.setScoreSection(objectiveSection);
                                    score.setHighestPoint(correctAnswer);
                                    score.setScorer(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

                                    storeUtil.setHighScores(score, failure, success);
                                }
                            }
                        };

                        OnFailureListener failed = new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                            }
                        };
                        storeUtil.getHighestScoresFor(objectiveSection, successful ,failed);
                    }
                });
                dialog.show();

            }

            @Override
            public void disable() {
                totalQuestion--;
                checkBox.setEnabled(false);
                checkBox2.setEnabled(false);
                checkBox3.setEnabled(false);
                checkBox4.setEnabled(false);

                checkStates.get(position).put("answered", true);

            }
        };

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change.disable();
                change.changeBkgColor(checkBox);

                checkStates.get(position).put("answered", true);
                checkStates.get(position).put("A", true);
            }
        });

        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change.disable();
                change.changeBkgColor(checkBox2);

                checkStates.get(position).put("answered", true);
                checkStates.get(position).put("B", true);
            }
        });

        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change.disable();
                change.changeBkgColor(checkBox3);

                checkStates.get(position).put("answered", true);
                checkStates.get(position).put("C", true);
            }
        });

        checkBox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change.disable();
                change.changeBkgColor(checkBox4);

                checkStates.get(position).put("answered", true);
                checkStates.get(position).put("D", true);
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

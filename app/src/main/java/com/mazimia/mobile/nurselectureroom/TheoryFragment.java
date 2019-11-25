package com.mazimia.mobile.nurselectureroom;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class TheoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SECID = "sectionId";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String sectionId;
    private CustomRecyclerView queRecycleView;
    private static TheoryQueAdapter queAdapter;
    private FireStoreUtil storeUtil;
    private ViewHolderClickListener listener;

//    private OnFragmentInteractionListener mListener;

    public TheoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param secId Parameter 1.
     * @return A new instance of fragment TheoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TheoryFragment createQueFragmentWithSectionId(String secId) {
        TheoryFragment fragment = new TheoryFragment();
        Bundle args = new Bundle();
        args.putString(SECID, secId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sectionId = getArguments().getString(SECID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // click listener
        listener = new ViewHolderClickListener() {
            @Override
            public void onClick(View view, int position) {
                //nothing
            }
        };


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_theory, container, false);
        if (sectionId != null) {
            queRecycleView = view.findViewById(R.id.theoryQueCustomRecyclerView);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.VERTICAL, false);
            queRecycleView.setLayoutManager(linearLayoutManager);
            if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("nurse.lectureroom@gmail.com"))
                registerForContextMenu(queRecycleView);
            queAdapter = new TheoryQueAdapter();
            queAdapter.setListener(listener);
            queRecycleView.setAdapter(queAdapter);
        }

        // Query Firestore database for theory questions
        storeUtil = new FireStoreUtil(FirebaseFirestore.getInstance());

        // success listener
        OnSuccessListener<QuerySnapshot> success = new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Question> questions = new ArrayList<>();
                for (Question ques : queryDocumentSnapshots.toObjects(Question.class)) {

                    questions.add(ques);
                }
                queAdapter.setQuestions(questions);
                Toast.makeText(getActivity(), "Successfully loaded your theory questions",
                        Toast.LENGTH_SHORT).show();
            }
        };

        // failure listener
        OnFailureListener failure = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Do something when it fails
                Toast.makeText(getActivity(), "Unable to load theory questions",
                        Toast.LENGTH_SHORT).show();
            }
        };


        storeUtil.getQuestions(sectionId, Question.THEO, success, failure);

        return view;
    }

    final int CONTEXT_THEORY_EDIT = 3;
    final int CONTEXT_THEORY_DELETE = 4;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        menu.setHeaderTitle("Theory Options");
        menu.add(Menu.NONE, CONTEXT_THEORY_EDIT, Menu.NONE, "Edit");
        menu.add(Menu.NONE, CONTEXT_THEORY_DELETE, Menu.NONE, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final CustomRecyclerView.RecyclerContextMenuInfo itemInfo =
                (CustomRecyclerView.RecyclerContextMenuInfo)item.getMenuInfo();

        final Question question = queAdapter.getQuestions().get(itemInfo.position);
        Intent intent = new Intent(getActivity(), CreateQuestionActivity.class);

        switch (item.getItemId()) {
            case (CONTEXT_THEORY_EDIT):
                intent.putExtra("isEdit", true);
                intent.putExtra("questionId", question.getId());
                intent.putExtra("sectionId", question.getSectionId());
                intent.putExtra("question", question.getQuestion());
                intent.putExtra("questionType", Question.THEO);

                startActivity(intent);
                return true;

            case (CONTEXT_THEORY_DELETE):
                final FireStoreUtil storeUtil = new FireStoreUtil(FirebaseFirestore.getInstance());
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Delete Operation");
                alert.setMessage("Do you really want to delete this question?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        final OnSuccessListener<Void> delSuccess = new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Notify user that delete operation was successful
                                dialog.dismiss();
                                Toast.makeText(getContext(), "Question has been successfully deleted",
                                        Toast.LENGTH_LONG).show();
//                                ArrayList<Question> ques = queAdapter.getQuestions();
//                                Question isRemoved = ques.remove(itemInfo.position);
//                                if (isRemoved != null)
//                                    queAdapter.setQuestions(ques);
                            }
                        };

                        final OnFailureListener delFailure = new OnFailureListener() {

                            @Override
                            public void onFailure(@NonNull Exception e) {

                                // do something on failure
                                dialog.dismiss();
                                Toast.makeText(getContext(), "Question could not be deleted",
                                        Toast.LENGTH_LONG).show();


                            }

                        };
                        storeUtil.deleteQuestion(question.getId(), delSuccess, delFailure);

                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();
                return true;
        }
        return true;
    }
}

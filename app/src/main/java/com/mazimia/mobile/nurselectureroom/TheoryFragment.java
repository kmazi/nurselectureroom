package com.mazimia.mobile.nurselectureroom;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
                Toast.makeText(getActivity(), "I love theory questions ooo",
                        Toast.LENGTH_SHORT).show();
            }
        };

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_theory, container, false);
        if (sectionId != null) {
            queRecycleView = view.findViewById(R.id.theoryQueCustomRecyclerView);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.VERTICAL, false);
            queRecycleView.setLayoutManager(linearLayoutManager);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);

    }
}

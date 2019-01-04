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
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ObjectiveFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SECTIONID = "sectionId";

    // TODO: Rename and change types of parameters
    private String sectionId;
    private CustomRecyclerView objRecyclerView;
    private ObjectiveQueAdapter objectiveQueAdapter;
    private FireStoreUtil storeUtil;
    private ViewHolderClickListener objectiveClickListener;

//    private OnFragmentInteractionListener mListener;

    public ObjectiveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param secId Parameter 1.
     * @return A new instance of fragment ObjectiveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ObjectiveFragment createObjFragment(String secId) {
        ObjectiveFragment fragment = new ObjectiveFragment();
        Bundle args = new Bundle();
        args.putString(SECTIONID, secId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sectionId = getArguments().getString(SECTIONID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        objectiveClickListener = new ViewHolderClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getActivity(), "Sup "+ String.valueOf(position),
                        Toast.LENGTH_SHORT).show();
            }
        };

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_objective, container, false);
        if (sectionId != null) {

            objRecyclerView = view.findViewById(R.id.objCustomRecyclerView);

            LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.VERTICAL, false);
            objRecyclerView.setLayoutManager(manager);
            registerForContextMenu(objRecyclerView);
            objectiveQueAdapter = new ObjectiveQueAdapter(getContext());

            objectiveQueAdapter.setListener(objectiveClickListener);

            objRecyclerView.setAdapter(objectiveQueAdapter);
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
                objectiveQueAdapter.setQuestions(questions);
                Toast.makeText(getActivity(), "Successfully loaded your objective questions",
                        Toast.LENGTH_SHORT).show();
            }
        };

        // failure listener
        OnFailureListener failure = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Do something when it fails
                Toast.makeText(getActivity(), "Unable to load objective questions",
                        Toast.LENGTH_SHORT).show();
            }
        };

        // query firebase for questions.
        storeUtil.getQuestions(sectionId, Question.OBJ, success, failure);

        return view;
    }

}

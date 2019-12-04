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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

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
//                Toast.makeText(getActivity(), "Sup "+ String.valueOf(position),
//                        Toast.LENGTH_SHORT).show();
            }
        };

        // Query Firestore database for theory questions
        storeUtil = new FireStoreUtil(FirebaseFirestore.getInstance());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_objective, container, false);

        if (sectionId != null) {

            objRecyclerView = view.findViewById(R.id.objCustomRecyclerView);

            LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.VERTICAL, false);
            objRecyclerView.setLayoutManager(manager);
            String adminMails = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            if (adminMails.equals("nurse.lectureroom@gmail.com") || adminMails.equals("kingsleyu13@yahoo.com"))
                registerForContextMenu(objRecyclerView);
            objectiveQueAdapter = new ObjectiveQueAdapter(getContext());

            objectiveQueAdapter.setListener(objectiveClickListener);

            // Getting the section title to use for updating highscores
            //
            ///
            //
            OnFailureListener failure = new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Couldn't tell which section to upload scores",
                            Toast.LENGTH_LONG).show();
                }
            };

            OnSuccessListener<DocumentSnapshot> success = new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    objectiveQueAdapter.setObjectiveSection(
                            documentSnapshot.toObject(Section.class).getTitle()
                    );
                }
            };
            storeUtil.getSection(sectionId, success, failure);

            objRecyclerView.setAdapter(objectiveQueAdapter);
        }

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

    final int CONTEXT_EDIT = 1;
    final int CONTEXT_DELETE = 2;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        menu.setHeaderTitle("Theory Options");
        menu.add(Menu.NONE, CONTEXT_EDIT, Menu.NONE, "Edit");
        menu.add(Menu.NONE, CONTEXT_DELETE, Menu.NONE, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        CustomRecyclerView.RecyclerContextMenuInfo itemInfo =
                (CustomRecyclerView.RecyclerContextMenuInfo)item.getMenuInfo();

        final Question question = objectiveQueAdapter.getQuestions().get(itemInfo.position);
        Intent intent = new Intent(getActivity(), CreateQuestionActivity.class);
        if(item.getItemId() != CONTEXT_EDIT && item.getItemId() != CONTEXT_DELETE)
            return false;
        switch (item.getItemId()) {
            case (CONTEXT_EDIT):
                intent.putExtra("isEdit", true);
                intent.putExtra("questionId", question.getId());
                intent.putExtra("sectionId", question.getSectionId());
                intent.putExtra("question", question.getQuestion());
                intent.putExtra("questionType", Question.OBJ);
                intent.putExtra("optionA", question.getOptions().get("A"));
                intent.putExtra("optionB", question.getOptions().get("B"));
                intent.putExtra("optionC", question.getOptions().get("C"));
                intent.putExtra("optionD", question.getOptions().get("D"));
                intent.putExtra("answer", question.getAnswer());
                startActivity(intent);
                return true;

            case (CONTEXT_DELETE):
                final FireStoreUtil storeUtil = new FireStoreUtil(FirebaseFirestore.getInstance());
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Delete Operation");
                alert.setMessage("Do you rely want to delete this question?");
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

//                                Boolean isRemoved = objectiveQueAdapter.getQuestions().remove(question);
//                                if (isRemoved)
//                                    objectiveQueAdapter.setQuestions(objectiveQueAdapter.getQuestions());
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

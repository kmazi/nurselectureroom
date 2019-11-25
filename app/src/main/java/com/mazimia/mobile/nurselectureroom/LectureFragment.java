package com.mazimia.mobile.nurselectureroom;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LectureFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LectureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LectureFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM2 = "param2";
    private TextView lecText;
    private TextView lecTitle;

    // TODO: Rename and change types of parameters
    private String lectureTitle;
    private String lectureNote;

//    private OnFragmentInteractionListener mListener;

    public LectureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getArguments() != null) {
            lectureTitle = getArguments().getString("lecTitle");
            lectureNote = getArguments().getString("lecNote");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lecture, container, false);
        lecText = view.findViewById(R.id.lecture_frag_txtview);
        lecTitle = view.findViewById(R.id.lecttitle_frag_txtview);

        lecText.setText(lectureNote);
        lecTitle.setText(lectureTitle);
        return view;
    }

}

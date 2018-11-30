package com.mazimia.mobile.nurselectureroom;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

// A utility class for interfacing with Firestore database
public class StoreUtil {

    private FirebaseFirestore database;
    private final String TAG = "Lecture section";
    private final String LECTURE_SECTIONS = "lecture-sections";
    private final String LECTURES = "lectures";

    public StoreUtil (FirebaseFirestore db) {
        database = db;
    }

    // Creates lecture sections
    public void createLectureSection(LectureSection lectureSection, OnSuccessListener<Void> successAction) {

        database.collection(LECTURE_SECTIONS)
                .document(lectureSection.getTitle())
                .set(lectureSection, SetOptions.merge())
                .addOnSuccessListener(successAction)
                .addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d("TAG", e.toString());

                    }

                });
    }


    public void createLecture(LectureSection section, Lecture lecture,
                              OnSuccessListener<Void> successAction) {

        Map<String, Object> lect = new HashMap<>();
                lect.put(LECTURES, Arrays.asList(lecture.getLecture()));
        database.collection(LECTURE_SECTIONS)
                .document(section.getTitle()).set(lect, SetOptions.merge())
                .addOnSuccessListener(successAction)
                .addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d("TAG", e.toString());

                    }

                });
    }


//    // Create a lecture for a section
//    public void createLecture(LectureSection section,
//                              Lecture lecture,
//                              OnSuccessListener<Void> onSuccess) {
//
//        DocumentReference lectureDoc = database.collection(LECTURE_SECTIONS)
//                .document(section.getTitle());
//
//        lectureDoc.collection(LECTURES)
//                .document(lecture.getTopic())
//                .set(lecture.getLecture(), SetOptions.merge())
//                .addOnSuccessListener(onSuccess)
//                .addOnFailureListener(new OnFailureListener() {
//
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                        Log.d("TAG", e.toString());
//                    }
//
//                });
//    }


    // Deletes a lecture section
//    public void deleteLectureSection(LectureSection lectureSection, OnSuccessListener<Void> successAction) {
//
//        database.collection(LECTURE_SECTIONS)
//                .document(lectureSection.getTitle())
//                .delete()
//                .addOnSuccessListener(successAction)
//                .addOnFailureListener(new OnFailureListener() {
//
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                        Log.d("TAG", e.toString());
//
//                    }
//
//                });
//    }


    // Delete a lecture for a section

}

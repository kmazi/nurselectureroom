package com.mazimia.mobile.nurselectureroom;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

// A utility class for interfacing with Firestore database
public class FireStoreUtil {

    private CollectionReference lectureSections;
    private CollectionReference lectures;

    private final String TAG = "Lecture section";
    private final String LECTURE_SECTIONS = "Sections";
    private final String LECTURES = "lectures";

    private ArrayList<Section> sections = new ArrayList<>();
    private Section section = new Section();
    private Lecture singleLecture = new Lecture();


    public FireStoreUtil(){

    }


    public FireStoreUtil(FirebaseFirestore db) {

        lectureSections = db.collection(LECTURE_SECTIONS);
        lectures = db.collection(LECTURES);
    }


    // Creates lecture section
    public void createLectureSection(Section section,
                                     final OnCompleteListener<Void> successAction,
                                     OnFailureListener failAction) {

        lectureSections.add(section)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String sectionRef = documentReference.getId();
                        documentReference.update(Section.ID, sectionRef).addOnCompleteListener(successAction);
                    }
                })
                .addOnFailureListener(failAction);
    }


    // Get a specific section
    public Section getSection(String id) {

        getLectureSection(id);
        return section;
    }


    // Update a lecture section
    public void updateSection(Section sec, OnSuccessListener<Void> success,
                              OnFailureListener failure) {

        lectureSections.document(sec.getId()).update(sec.updateSectionData())
                .addOnSuccessListener(success)
        .addOnFailureListener(failure);

    }

    // Deletes a lecture section
    public void deleteSection(final String secId, final OnCompleteListener<Void> success,
                              final OnFailureListener failure) {
        lectures.whereEqualTo(Lecture.SECTION_ID, secId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Delete all documents in a section first before deleting the section
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc : docs) {
                            doc.getReference().delete();
                        }

                        lectureSections.document(secId).delete()
                                .addOnCompleteListener(success)
                                .addOnFailureListener(failure);
                    }
                })
                .addOnFailureListener(failure);

    }


    // Get a particular lecture section
    public void getLectureSection(String sectionId) {

        lectureSections.document(sectionId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        section = documentSnapshot.toObject(Section.class);
                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // do something when it fails
            }
        });

    }


    // populate the sections field with section data
    public void getLectureSections(OnCompleteListener<QuerySnapshot> successListener) {

        lectureSections.get().addOnCompleteListener(successListener);
    }


    // Creates lecture for a section
    public void createLecture(final Lecture lecture,
                              final OnSuccessListener<DocumentReference> success,
                              final OnFailureListener failure) {

        lectureSections.document(lecture.getSectionId()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    lectures.add(lecture)
                            .addOnSuccessListener(success)
                            .addOnFailureListener(failure);
                } else {

                    // do something if the section don't exist
                }
            }
        });

    }


    //get all lectures for a section
    public void getLectures(String sectionId, OnSuccessListener<QuerySnapshot> success,
                            OnFailureListener failure) {

        lectures.whereEqualTo(Lecture.SECTION_ID, sectionId)
                .get().addOnSuccessListener(success).addOnFailureListener(failure);
    }


    // get lecture from a section
    public Lecture getLecture(Lecture lecture) {

        lectures.document(lecture.getId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        FireStoreUtil.this.singleLecture = documentSnapshot.toObject(Lecture.class);
                    }
                });
        return singleLecture;
    }


    // Updates a lecture
    public void updateLecture(Lecture lecture, OnSuccessListener success,
                              OnFailureListener failure) {

        lectures.document(lecture.getId())
                .update(lecture.updateLectureData())
                .addOnSuccessListener(success)
                .addOnFailureListener(failure);
    }


    // Delete a lecture
    public void deleteLecture(String lecId, OnSuccessListener<Void> success,
                              OnFailureListener failure) {

        lectureSections.document(lecId)
                .delete()
                .addOnSuccessListener(success)
                .addOnFailureListener(failure);

    }

}

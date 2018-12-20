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
    public void updateSection(Section sec) {

        lectureSections.document(sec.getId()).update(sec.updateSectionData())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // do something when successful
                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // do something when it fails.
            }
        });

    }

    // Deletes a lecture section
    public void deleteSection(String secId) {

        lectureSections.document(secId).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // do something when its successful
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // do something when it fails.
                    }
                });

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
    public void createLecture(final Lecture lecture) {

        lectureSections.document(lecture.getSectionId()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    lectures.add(lecture)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    String lectureRef = documentReference.getId();
                                    documentReference.update(Lecture.ID, lectureRef)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    // update the user on successful doc creation
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // do something when doc creation fails.
                                }
                            });
                } else {

                    // do something if the section don't exist
                }
            }
        });

    }


    //get all lectures for a section
    public ArrayList<Lecture> getLectures(Lecture lecture) {

        final ArrayList<Lecture> secLectures = new ArrayList<Lecture>();

        lectures.whereEqualTo(Lecture.SECTION_ID, lecture.getSectionId())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (Lecture lecture : queryDocumentSnapshots.toObjects(Lecture.class)) {

                    secLectures.add(lecture);
                }
            }
        });
        return secLectures;
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
    public void updateLecture(Lecture lecture) {

        lectures.document(lecture.getId())
                .update(lecture.updateLectureData())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {

                        // do something on failure

                    }

                });
    }


    // Delete a lecture
    public void deleteLecture(Lecture lecture) {

        lectureSections.document(lecture.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Notify user that delete operation was successful
                    }
                })
                .addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {

                        // do something on failure

                    }

                });

    }

}

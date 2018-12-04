package com.mazimia.mobile.nurselectureroom;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// A utility class for interfacing with Firestore database
public class FireStoreUtil {

    private CollectionReference lectureSections;
    private final String TAG = "Lecture section";
    private final String LECTURE_SECTIONS = "Sections";
    private final String LECTURES = "lectures";

    private static ArrayList<Section> sections = new ArrayList<>();
    private Map<String, Object> section = new HashMap<>();

    public FireStoreUtil(FirebaseFirestore db) {

        lectureSections = db.collection(LECTURE_SECTIONS);
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


    // Get all lecture sections
    public ArrayList<Section> getSections() {

        // populate the sections field with data from fire base
        getLectureSections();
        return sections;
    }

    // Get a specific section
    public Map<String, Object> getSection(String title) {

        getLectureSection(title);
        return section;
    }


    // Update a lecture section
    public void updateLectureSection(String title, final Section sec) {

        lectureSections.whereEqualTo(Section.TITLE, title)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot docSnapshot : task.getResult()) {

                                docSnapshot.getReference().update(sec.createSection())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {

                                                    // leave a message for the user
                                                } else {

                                                    // let the user know they failed
                                                }
                                            }
                                        });
                                break;
                            }
                        }
                    }
                });
    }

    // Deletes a lecture section
    public void deleteLectureSection(Section sec) {

        lectureSections.whereEqualTo(Section.TITLE, sec.getTitle())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot docSnapshot : task.getResult()) {

                                docSnapshot.getReference().delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {

                                                    // leave a message for the user
                                                } else {

                                                    // let the user know they failed
                                                }
                                            }
                                        });
                                break;
                            }
                        }
                    }
                });

    }


    // Get a particular lecture section
    public void getLectureSection(String sectionTitle) {
        lectureSections.whereEqualTo(Section.TITLE, sectionTitle)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot doc : task.getResult()) {

                                section = doc.getData();
                                break;
                            }
                        } else {

                            // do something when it fails
                        }
                    }
                });

    }


    // populate the sections field with section data
    public void getLectureSections() {

        lectureSections.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        sections.add(document.toObject(Section.class));
                    }

                } else {

                    // do something if it fails
                }
            }
        });
    }


    // Creates lecture for a section
    public void createLecture(Section section, Lecture lecture,
                              OnSuccessListener<Void> successAction) {

        Map<String, Object> newLecture = new HashMap<>();
                newLecture.put(LECTURES, Arrays.asList(lecture.getLecture()));

        lectureSections.document(section.getTitle()).set(newLecture, SetOptions.merge())
                .addOnSuccessListener(successAction)
                .addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {

                        // Do something on failure

                    }

                });
    }


    // Updates a lecture
    public void updateLecture(Section section, Lecture lecture,
                              OnSuccessListener<Void> successAction) {

        lectureSections.document(section.getTitle())
                .update(LECTURES, FieldValue.arrayUnion(lecture.getLecture()))
                .addOnSuccessListener(successAction)
                .addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {

                        // do something on failure

                    }

                });
    }


    // get lecture from a section
    public void getLecture(Section section) {

        lectureSections.document(section.getTitle())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        documentSnapshot.get(LECTURES);
                    }
                });
    }


    // Delete a lecture for a section
    public void deleteLecture(Section section, Lecture lecture,
                              OnSuccessListener<Void> successAction) {

        lectureSections.document(section.getTitle())
                .update(LECTURES, FieldValue.arrayRemove(lecture.getLecture()))
                .addOnSuccessListener(successAction)
                .addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {

                        // do something on failure

                    }

                });

    }

}

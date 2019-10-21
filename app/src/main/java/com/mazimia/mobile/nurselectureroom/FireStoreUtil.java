package com.mazimia.mobile.nurselectureroom;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

// A utility class for interfacing with Firestore database
public class FireStoreUtil {

    private CollectionReference sections;
    private CollectionReference lectures;
    private CollectionReference questions;
    private CollectionReference users;
    private CollectionReference highestScores;

    private final String LECTURE_SECTIONS = "Sections";
    private final String LECTURES = "Lectures";
    private final String QUESTIONS = "Questions";
    private final String USERS = "Users";
    private final String SCORES = "Scores";

    private Lecture singleLecture = new Lecture();



    public FireStoreUtil(FirebaseFirestore db) {

        sections = db.collection(LECTURE_SECTIONS);
        lectures = db.collection(LECTURES);
        questions = db.collection(QUESTIONS);
        users = db.collection(USERS);
        highestScores = db.collection(SCORES);
    }

    // Creates users
    public void createUser(Map<String, Object> user, OnSuccessListener<Void> success,
                           OnFailureListener failure) {
        users.document(user.get("token").toString())
        .set(user).addOnSuccessListener(success).addOnFailureListener(failure);
    }


    // Creates lecture section
    public void createLectureSection(Section section,
                                     final OnSuccessListener<Void> successAction,
                                     OnFailureListener failAction) {
        DocumentReference lecSection = sections.document();
        section.setId(lecSection.getId());
        lecSection.set(section).addOnSuccessListener(successAction).addOnFailureListener(failAction);
    }


    // Update a lecture section
    public void updateSection(Section sec, OnSuccessListener<Void> success,
                              OnFailureListener failure) {

        sections.document(sec.getId()).update(sec.updateSectionData())
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
                        questions.whereEqualTo(Question.SECTION_ID, secId).get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (DocumentSnapshot que : queryDocumentSnapshots.getDocuments()) {
                                            que.getReference().delete();
                                        }
                                        sections.document(secId).delete()
                                                .addOnCompleteListener(success)
                                                .addOnFailureListener(failure);
                                    }
                                })
                                .addOnFailureListener(failure);

                    }
                })
                .addOnFailureListener(failure);

    }


    // populate the sections field with section data
    public void getLectureSections(OnCompleteListener<QuerySnapshot> successListener) {

        sections.get().addOnCompleteListener(successListener);
    }

    // get a particular section
    public void getSection(String sectionId, OnSuccessListener<DocumentSnapshot> success,
                           OnFailureListener failure) {
        sections.document(sectionId).get().addOnSuccessListener(success)
                .addOnFailureListener(failure);
    }


    // Creates lecture for a section
    public void createLecture(final Lecture lecture,
                              final OnSuccessListener<Void> success,
                              final OnFailureListener failure) {

        sections.document(lecture.getSectionId()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    DocumentReference newLecture = lectures.document();
                    lecture.setId(newLecture.getId());
                    newLecture.set(lecture).addOnSuccessListener(success)
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

        lectures.document(lecId)
                .delete()
                .addOnSuccessListener(success)
                .addOnFailureListener(failure);

    }


    //////////////////////////////////////////////////////////////////////////////////////////////
    // Question section
    //////////////////////////////////////////////////////////////////////////////////////////////

    public void createQuestion(final Question question,
                               final OnSuccessListener<Void> complete,
                               final OnFailureListener failure) {

        sections.document(question.getSectionId()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            DocumentReference new_question = questions.document();
                            question.setId(new_question.getId());
                            new_question.set(question).addOnSuccessListener(complete)
                                    .addOnFailureListener(failure);

                        } else {

                            // do something if the section don't exist
                        }
                    }
                });

    }

    //get all questions for a section
    public void getQuestions(String sectionId, String type, OnSuccessListener<QuerySnapshot> success,
                             OnFailureListener failure) {

        questions.whereEqualTo(Question.SECTION_ID, sectionId)
                .whereEqualTo(Question.TYPE, type)
                .get().addOnSuccessListener(success)
                    .addOnFailureListener(failure);
    }

    // Updates a lecture
    public void updateQuestion(Question question, OnSuccessListener<Void> success,
                               OnFailureListener failure) {

        questions.document(question.getId())
                .update(question.updateQuestionData())
                .addOnSuccessListener(success)
                .addOnFailureListener(failure);
    }

    // Delete a question
    public void deleteQuestion(String id, OnSuccessListener<Void> success,
                               OnFailureListener failure) {

        questions.document(id)
                .delete()
                .addOnSuccessListener(success)
                .addOnFailureListener(failure);

    }

    //////////////////////////=========== HIGHEST SCORES ==============/////////////////////////////
    // Get Highest scores.
    public void getHighestScores(OnSuccessListener<QuerySnapshot> success, OnFailureListener failure) {
        highestScores.get().addOnSuccessListener(success)
                .addOnFailureListener(failure);
    }

    // Set Highest score
    public void setHighScores(HighScore score, OnFailureListener failure,
                              OnSuccessListener<Void> success) {
        DocumentReference scoreDoc =  highestScores.document();
        score.setId(scoreDoc.getId());
        scoreDoc.set(score).addOnFailureListener(failure).addOnSuccessListener(success);
    }

    // Get Highest score for a particular section
    public void getHighestScoresFor(String sectionTitle, OnSuccessListener<QuerySnapshot> success,
                                    OnFailureListener failure) {
        highestScores.whereEqualTo("scoreSection", sectionTitle).get().addOnFailureListener(
                failure).addOnSuccessListener(success);
    }
}

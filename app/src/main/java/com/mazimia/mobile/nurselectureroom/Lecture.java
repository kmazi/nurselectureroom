package com.mazimia.mobile.nurselectureroom;

import com.google.firebase.firestore.model.value.NullValue;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


class Lecture {

    private String topic;
    private String description;
    private String note;
    private Map<String, Object> lecture;

    //public final String TIMESTAMP = "timestamp";
    public final static String TITLE = "title";
    public final static String SUMMARY = "summary";
    public final static String NOTE = "note";

    public Lecture (String topic, String description) {

        this.topic = topic;
        this.description = description;
        this.note = "";
        this.lecture = new HashMap<>();
    }

    public Lecture (String topic, String description, String note) {

        this.topic = topic;
        this.description = description;
        this.note = note;
        this.lecture = new HashMap<>();
    }


    // getter for topic
    public String getTopic() {
        return topic;
    }


    // setter for topic
    public void setTopic(String topic) {
        this.topic = topic;
    }


    // getter for description
    public String getDescription() {
        return description;
    }


    // setter for description
    public void setDescription(String description) {
        this.description = description;
    }


    // getter for note
    public String getNote() {
        return note;
    }


    // setter for note
    public void setNote(String note) {
        this.note = note;
    }


    // Get the lectures
    public Map<String, Object> getLecture() {
        return lecture;
    }


    // create the lectures for a section
    public void setLecture() {

        lecture.put(TITLE, topic);
        lecture.put(SUMMARY, description);
        lecture.put(NOTE, note);
        //lecture.put(TIMESTAMP, Calendar.getInstance().getTime());

    }

}

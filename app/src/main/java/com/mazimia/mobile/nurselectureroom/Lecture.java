package com.mazimia.mobile.nurselectureroom;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


class Lecture {

    private String topic;
    private String description;
    private String note;
    private String sectionId;
    private String id;
    private String createdAt;

    private Map<String, Object> lecture;

    public final String CREATED_AT = "createdAt";
    public final static String TOPIC = "topic";
    public final static String DESCRIPTION = "description";
    public final static String NOTE = "note";
    public final static String ID = "id";
    public final static String SECTION_ID = "sectionId";


    public Lecture() {

    }


    public Lecture (String topic, String description, String note) {

        this.topic = topic;
        this.description = description;
        this.note = note;
        this.createdAt = Calendar.getInstance().getTime().toString();
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
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


    // create the lecture for a section
    public Map<String, Object> updateLectureData() {

        Map<String, Object> lecture = new HashMap<>();
        if (!this.topic.equals("") && this.topic != null) {
            lecture.put(TOPIC, getTopic());
        }

        if (!this.description.equals("") && this.description != null) {

            lecture.put(DESCRIPTION, getDescription());
        }

        if (!this.note.equals("") && this.note != null) {

            lecture.put(NOTE, getNote());
        }

        lecture.put(CREATED_AT, Calendar.getInstance().getTime().toString());
        return lecture;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

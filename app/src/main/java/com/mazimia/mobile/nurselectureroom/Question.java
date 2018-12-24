package com.mazimia.mobile.nurselectureroom;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Question {

    private String sectionId;
    private String id;
    private String question;
    private String type;
    private Map<String, String> options;
    private Character answer;
    private String createdAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    final static String ID = "id";
    final static String SECTION_ID = "sectionId";
    final static String TYPE = "type";
    final static String OPTIONS = "options";
    final static String ANSWER = "answer";
    final static String CREATED_AT = "createdAt";

    public String getType() {
        return type;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public Character getAnswer() {
        return answer;
    }

    public void setAnswer(Character answer) {
        this.answer = answer;
    }

    public void setType(String type) {
        this.type = type;
    }

    final static String QUESTION = "question";

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Question() {

        this.sectionId = "";
        this.id = "";
    }

    public String getSectionId() {
        return this.sectionId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // create the question for a section
    public Map<String, Object> updateQuestionData() {

        Map<String, Object> question = new HashMap<>();
        if (!this.question.equals("") && this.question != null) {
            question.put(QUESTION, getQuestion());
        }

        if (!this.type.equals("") && this.type != null) {

            question.put(TYPE, getType());
        }

        if (this.options != null) {

            question.put(OPTIONS, getOptions());
        }

        if (!this.sectionId.equals("") && this.sectionId != null) {

            question.put(SECTION_ID, getSectionId());
        }

        if (this.answer != null) {

            question.put(ANSWER, getAnswer());
        }
        question.put(CREATED_AT, Calendar.getInstance().getTime());
        return question;
    }
}

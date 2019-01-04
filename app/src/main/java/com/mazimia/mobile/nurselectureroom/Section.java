package com.mazimia.mobile.nurselectureroom;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class Section {

    private String title;
    private String thumbnail;
    private String summary;
    private String createdAt;
    private String id;

    public final static String TITLE = "title";
    public final static String SUMMARY = "summary";
    public final static String THUMBNAIL = "thumbnail";
    public final static String CREATEDAT = "createdAt";
    public final static String ID = "id";


    public Section() {

    }

    // lecture section constructor
    public Section(String section_title) {
        this.title = section_title;
        this.thumbnail = "";
        this.summary = "";
        this.createdAt = Calendar.getInstance().getTime().toString();
        this.id = "";
    }

    // lecture section constructor
    public Section(String section_title, String section_summary) {
        this.title = section_title;
        this.thumbnail = "";
        this.summary = section_summary;
        this.createdAt = Calendar.getInstance().getTime().toString();
        this.id = "";
    }

    // lecture section constructor
    public Section(String section_title, String section_summary, String imageUrl) {
        this.title = section_title;
        this.thumbnail = imageUrl;
        this.summary = section_summary;
        this.createdAt = Calendar.getInstance().getTime().toString();
        this.id = "";
    }

    // provides access to the title of the section
    public String getTitle() {
        return title;
    }

    // provides access to the summary of the section
    public String getSummary() {
        return summary;
    }

    // provides access to the thumbnail of the section
    public String getThumbnail() {
        return thumbnail;
    }

    // set the value of title
    public void setTitle(String title) {
        this.title = title;
    }

    // set the value of summary
    public void setSummary (String summary) {
        this.summary = summary;
    }

    // set the value of section thumbnail
    public void setThumbnail (String thumbnail) {
        this.thumbnail = thumbnail;
    }

    // get section id
    public String getId() {
        return id;
    }

    // set section id
    public void setId(String id) {
        this.id = id;
    }

    // get last update for section
    public String getCreatedAt() {
        return createdAt;
    }

    // create the document to add to a collection
    public Map<String, Object> updateSectionData() {

        Map<String, Object> section = new HashMap<>();
        if (!this.title.equals("") && !this.title.equals(null)) {
            section.put(TITLE, getTitle());
        }

        if (!this.summary.equals("") && !this.summary.equals(null)) {

            section.put(SUMMARY, getSummary());
        }

        if (!this.thumbnail.equals("") && !this.thumbnail.equals(null)) {

            section.put(THUMBNAIL, getThumbnail());
        }
        section.put(CREATEDAT, Calendar.getInstance().getTime());
        return section;
    }
}

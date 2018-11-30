package com.mazimia.mobile.nurselectureroom;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

class LectureSection {

    private String title;
    private String thumbnail;
    private String summary;

    public final static String TITLE = "title";
    public final static String SUMMARY = "summary";
    public final static String THUMBNAIL = "thumbnail_url";
    public final static String TIMESTAMP = "timestamp";


    // lecture section constructor
    public LectureSection(String section_title, String section_summary) {
        title = section_title;
        thumbnail = "";
        summary = section_summary;
    }

    // lecture section constructor
    public LectureSection(String section_title, String section_summary, String imageUrl) {
        title = section_title;
        thumbnail = imageUrl;
        summary = section_summary;
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


    // create the document to add to a collection
    public Map<String, Object> createSection() {

        Map<String, Object> section = new HashMap<>();
        section.put(TITLE, getTitle());
        section.put(SUMMARY, getSummary());
        section.put(THUMBNAIL, getThumbnail());
        section.put(TIMESTAMP, Calendar.getInstance().getTime());
        return section;
    }
}

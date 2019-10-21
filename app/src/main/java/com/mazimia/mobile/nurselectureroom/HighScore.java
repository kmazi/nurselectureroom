package com.mazimia.mobile.nurselectureroom;

class HighScore {
    private int highestPoint;
    private String id;
    private String scorer;
    private String scoreSection;

    public int getHighestPoint() {
        return highestPoint;
    }

    public void setHighestPoint(int highestPoint) {
        this.highestPoint = highestPoint;
    }

    public String getScorer() {
        return scorer;
    }

    public void setScorer(String scorer) {
        this.scorer = scorer;
    }

    public String getScoreSection() {
        return scoreSection;
    }

    public void setScoreSection(String scoreSection) {
        this.scoreSection = scoreSection;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

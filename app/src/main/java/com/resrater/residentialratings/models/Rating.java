package com.resrater.residentialratings.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Rating {

    private String feedback, userID;
    private int score;
    private @ServerTimestamp Date timestamp;

    public Rating(String feedback, String userID, int score, Date timestamp) {
        this.feedback = feedback;
        this.userID = userID;
        this.score = score;
        this.timestamp = timestamp;
    }

    public Rating() {
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}


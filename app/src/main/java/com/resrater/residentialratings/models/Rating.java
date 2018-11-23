package com.resrater.residentialratings.models;

import android.location.Address;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Rating {

    private String feedback, userID, ratingID;
    private int score;
    private @ServerTimestamp Date timestamp;
    private android.location.Address address;

    public Rating(String feedback, String userID, String ratingID, int score, Date timestamp, Address address) {
        this.feedback = feedback;
        this.userID = userID;
        this.ratingID = ratingID;
        this.score = score;
        this.timestamp = timestamp;
        this.address = address;
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

    public String getRatingID() {
        return ratingID;
    }

    public void setRatingID(String ratingID) {
        this.ratingID = ratingID;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}

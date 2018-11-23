package com.resrater.residentialratings.models;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Rating {

    private String feedback, userID, address;
    private int score;
    private @ServerTimestamp Date timestamp;
    private GeoPoint mapLocation;

    public Rating(String feedback, String userID, String address, int score, Date timestamp, GeoPoint mapLocation) {
        this.feedback = feedback;
        this.userID = userID;
        this.address = address;
        this.score = score;
        this.timestamp = timestamp;
        this.mapLocation = mapLocation;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public GeoPoint getMapLocation() {
        return mapLocation;
    }

    public void setMapLocation(GeoPoint mapLocation) {
        this.mapLocation = mapLocation;
    }
}


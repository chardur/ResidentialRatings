package com.resrater.residentialratings.models;


import com.google.firebase.firestore.GeoPoint;

public class User {

    private String userID, email;
    private GeoPoint homeAddress;

    public User(String userID, String email, GeoPoint homeAddress) {
        this.userID = userID;
        this.email = email;
        this.homeAddress = homeAddress;
    }

    public User() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public GeoPoint getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(GeoPoint homeAddress) {
        this.homeAddress = homeAddress;
    }
}

package com.resrater.residentialratings.models;

import com.google.firebase.firestore.GeoPoint;

public class Residence {

    private String address;
    double avgRating;
    int numRatings;
    GeoPoint mapLocation;

    public Residence(String address, double avgRating, int numRatings, GeoPoint mapLocation) {
        this.address = address;
        this.avgRating = avgRating;
        this.numRatings = numRatings;
        this.mapLocation = mapLocation;
    }

    public Residence() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }

    public GeoPoint getMapLocation() {
        return mapLocation;
    }

    public void setMapLocation(GeoPoint mapLocation) {
        this.mapLocation = mapLocation;
    }
}

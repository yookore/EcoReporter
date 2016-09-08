package com.hyperswift.android.ecoreporter.models;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@IgnoreExtraProperties
public class Incident {
    private String body;
    private ArrayList<String> imageUris;
    private String location;
    private long creationDate;

    public Incident(){
        imageUris = new ArrayList<>();
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getImageUris() {
        return imageUris;
    }

    public void setImageUris(ArrayList<String> imageUris) {
        this.imageUris = imageUris;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public long getCreationDate() {
        return creationDate;
    }

    @Override
    public String toString() {
        return "Incident{" +
                "body='" + body + '\'' +
                ", imageUris=" + imageUris +
                ", location='" + location + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}

package com.example.meetgo;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by liuzulin on 12/5/17.
 */

public class cards {
    private String userId;
    private String post;
    private String zipcode;
    private Double lat;
    private Double lng;

    public cards (String userId , String post , Double lat, Double lng){
        this.userId = userId;
        this.post = post;
        this.lat = lat;
        this.lng = lng;
    }

    public String getUserId(){
        return userId;
    }
    public void setUserID(String userID){
        this.userId = userId;
    }

    public String getPost(){
        return post;
    }
    public void setPost(String post){
        this.post = post;
    }

    public Double getLat(){
        return lat;
    }
    public void setLat(String post){
        this.lat = lat;
    }
    public Double getLng(){
        return lng;
    }
    public void setLng(String post){
        this.lng = lng;
    }
}

package com.esprit.foodwin.Entity;

public class Reservation {

    private String id;

    public String getId() {
        return id;
    }

    public void setId( String id) {
        this.id = id;
    }

    private String time;
    private String nbrppl;
    private String date;
    private String username;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    private String userimage;

    public Reservation() {
    }

    public Reservation(String time, String nbrppl, String date) {
        this.time = time;
        this.nbrppl = nbrppl;
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNbrppl() {
        return nbrppl;
    }

    public void setNbrppl(String nbrppl) {
        this.nbrppl = nbrppl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
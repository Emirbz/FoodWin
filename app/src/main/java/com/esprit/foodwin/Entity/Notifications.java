package com.esprit.foodwin.Entity;

public class Notifications {
    private  String id;
    private  String title;
    private  String text;
    private  String idUser;
    private  String image;
    private  String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public Notifications() {

    }

    public Notifications(String id, String title, String text, String idUser, String image, String date) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.idUser = idUser;
        this.image = image;
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

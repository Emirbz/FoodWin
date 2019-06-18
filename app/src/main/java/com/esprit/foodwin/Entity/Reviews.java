package com.esprit.foodwin.Entity;

public class Reviews {
    private String commmentaire;
    private double quality;
    private double service;
    private String idEtab;
    private String idUser;
    private String nameUser;
    private  String imgUser;
    private  String date;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgUser() {
        return imgUser;
    }

    public void setImgUser(String imgUser) {
        this.imgUser = imgUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getIdEtab() {
        return idEtab;
    }

    public void setIdEtab(String idEtab) {
        this.idEtab = idEtab;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Reviews()
    {

    }

    public Reviews(String idEtab,String idUser,String commmentaire, double quality, double service) {
        this.idEtab = idEtab;
        this.idUser = idUser;
        this.commmentaire = commmentaire;
        this.quality = quality;
        this.service = service;
    }

    public String getCommmentaire() {

        return commmentaire;
    }

    public void setCommmentaire(String commmentaire) {
        this.commmentaire = commmentaire;
    }

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }

    public double getService() {
        return service;
    }

    public void setService(double service) {
        this.service = service;
    }
}

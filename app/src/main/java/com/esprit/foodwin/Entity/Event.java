package com.esprit.foodwin.Entity;

public class Event {

    private String id;
    private String name;
    private String starttime;
    private String endtime;
    private String date;
    private String alchool;
    private String image;
    private String description;
    private String fee;

    private String estabName;
    private String etabimg;
    private String etabadr;

    public String getEstabName() {
        return estabName;
    }

    public void setEstabName(String estabName) {
        this.estabName = estabName;
    }

    public String getEtabimg() {
        return etabimg;
    }

    public void setEtabimg(String etabimg) {
        this.etabimg = etabimg;
    }

    public String getEtabadr() {
        return etabadr;
    }

    public void setEtabadr(String etabadr) {
        this.etabadr = etabadr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getAlchool() {
        return alchool;
    }

    public void setAlchool(String alchool) {
        this.alchool = alchool;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

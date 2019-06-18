package com.esprit.foodwin.Entity;


public class Etablissement  {

    private String name;
    private String image;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String date;
    private int nbrRev;
    private String lat;

    public java.lang.String getLat() {
        return lat;
    }

    public void setLat(java.lang.String lat) {
        this.lat = lat;
    }

    public java.lang.String getLon() {
        return lon;
    }

    public void setLon(java.lang.String lon) {
        this.lon = lon;
    }

    private String lon;

    public int getNbrRev() {
        return nbrRev;
    }

    public void setNbrRev(int nbrRev) {
        this.nbrRev = nbrRev;
    }

    public java.lang.String getDate() {
        return date;
    }

    public void setDate(java.lang.String date) {
        this.date = date;
    }

    public java.lang.String getImg1() {
        return img1;
    }

    public void setImg1(java.lang.String img1) {
        this.img1 = img1;
    }

    public java.lang.String getImg2() {
        return img2;
    }

    public void setImg2(java.lang.String img2) {
        this.img2 = img2;
    }

    public java.lang.String getImg3() {
        return img3;
    }

    public void setImg3(java.lang.String img3) {
        this.img3 = img3;
    }

    public java.lang.String getImg4() {
        return img4;
    }

    public void setImg4(java.lang.String img4) {
        this.img4 = img4;
    }

    public java.lang.String getImg5() {
        return img5;
    }

    public void setImg5(java.lang.String img5) {
        this.img5 = img5;
    }

    private String img5;
    private double totalquality;
    private double quality;
    private int nbrreviews;

    public int getNbrreviews() {
        return nbrreviews;
    }

    public void setNbrreviews(int nbrreviews) {
        this.nbrreviews = nbrreviews;
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

    private double service;


    public double getTotalquality() {
        return totalquality;
    }

    public void setTotalquality(double totalquality) {
        this.totalquality = totalquality;
    }

    public double getTotalservice() {
        return totalservice;
    }

    public void setTotalservice(double totalservice) {
        this.totalservice = totalservice;
    }

    private double totalservice;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }

    public String getClimatisation() {
        return climatisation;
    }

    public void setClimatisation(String climatisation) {
        this.climatisation = climatisation;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getTerrace() {
        return terrace;
    }

    public void setTerrace(String terrace) {
        this.terrace = terrace;
    }

    public String getAlchool() {
        return alchool;
    }

    public void setAlchool(String alchool) {
        this.alchool = alchool;
    }

    public String getDebitcard() {
        return debitcard;
    }

    public void setDebitcard(String debitcard) {
        this.debitcard = debitcard;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getClosetime() {
        return closetime;
    }

    public void setClosetime(String closetime) {
        this.closetime = closetime;
    }
    private int String;

    public int getString() {
        return String;
    }

    public void setString(int string) {
        String = string;
    }
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String region;
    private double rating;
    private String subCategory;
    private String phone;
    private String email;
    private String address;
    private String wifi;
    private String climatisation;
    private String parking;
    private String terrace;
    private String alchool;
    private String debitcard;
    private String opentime;
    private String closetime;

    public Etablissement() {
    }

    public Etablissement(int id,String name, String image, String region, int rating, String subCategory, String phone, String email, String address, String wifi, String climatisation, String parking, String terrace, String alchool, String debitcard, String opentime, String closetime) {
        this.id=id;
        this.name = name;
        this.image = image;
        this.region = region;
        this.rating = rating;
        this.subCategory = subCategory;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.wifi = wifi;
        this.climatisation = climatisation;
        this.parking = parking;
        this.terrace = terrace;
        this.alchool = alchool;
        this.debitcard = debitcard;
        this.opentime = opentime;
        this.closetime = closetime;
    }

    public Etablissement(String name, String image, String region, double rating, String subCategory) {
        this.name = name;
        this.image = image;
        this.region = region;
        this.rating = rating;

        this.subCategory = subCategory;



    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
}

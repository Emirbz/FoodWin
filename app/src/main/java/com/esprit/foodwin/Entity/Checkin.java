package com.esprit.foodwin.Entity;

public class Checkin {
    private String idEtab;
    private String idUser;

    public Checkin()
    {

    }
    public Checkin(String idEtab, String idUser) {
        this.idEtab = idEtab;
        this.idUser = idUser;
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
}

package com.example.keepmoneyv3.utility;

import java.io.Serializable;

/**
 * A class that identifies an entry
 *
 * @author Michelangelo De Pascale
 * */

public class Entry implements Serializable {
    private int id;
    private float value;
    private String idUser, idCat;
    private String date;

    public Entry(int id, String idUser, String idCat, String date, float value) {
        this.id = id;
        this.idUser = idUser;
        this.idCat = idCat;
        this.date = date;
        this.value = value;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getIdCat() {
        return idCat;
    }

    public String getDate() {
        return date;
    }

    public float getValue() {
        return value;
    }

    public int getId() {
        return id;
    }
}

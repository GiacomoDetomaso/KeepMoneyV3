package com.example.keepmoneyv3.utility;

import java.io.Serializable;

public class Items implements Serializable {
    private int id;
    private int amount;
    private int valid;
    private String name;
    private float price;
    private String catID;

    public Items(String name, int amount, int valid, float price, String catID) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.catID = catID;
        this.valid = valid;
    }

    public Items(String name, int amount, int valid , float price) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.valid = valid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public float getPrice() {
        return price;
    }

    public String getCatID() {
        return catID;
    }

    public void setCatID(String catID) {
        this.catID = catID;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    public int getValid() {
        return valid;
    }
}

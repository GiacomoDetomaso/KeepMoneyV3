package com.example.keepmoneyv3.utility;

import java.io.Serializable;

public class Item implements Serializable {
    private int id;
    private int amount;
    private int valid;
    private String name;
    private float price;
    private String catID;

    public Item(String name, int amount, int valid, float price, String catID) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.catID = catID;
        this.valid = valid;
    }

    public Item(int id, String name, int amount, int valid , float price, String catID) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.valid = valid;
        this.catID = catID;
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

    public void setValid(int valid) {
        this.valid = valid;
    }

    public int getValid() {
        return valid;
    }
}

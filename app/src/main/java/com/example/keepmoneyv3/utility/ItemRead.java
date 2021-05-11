package com.example.keepmoneyv3.utility;

import java.io.Serializable;

public class ItemRead implements Serializable {
    private String itemName;
    private float itemValue;

    public ItemRead(String itemName,float itemValue){
        this.itemName = itemName;
        this.itemValue = itemValue;
    }

    public float getItemValue() {
        return itemValue;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemValue(float itemValue) {
        this.itemValue = itemValue;
    }
}

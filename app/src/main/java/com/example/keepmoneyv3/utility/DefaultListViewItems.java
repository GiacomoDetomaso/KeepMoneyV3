package com.example.keepmoneyv3.utility;

import java.io.Serializable;

/**
 * This class represents the purchases' information that will be displayed in the ListView.
 *
 * @author Michelangelo De Pascale
 * */
public  class DefaultListViewItems implements Serializable {
    private final int id;
    private final String itemName;
    private final int image;
    private final float price;

    public DefaultListViewItems(int id, String itemName, int image, float price) {
        this.id = id;
        this.itemName = itemName;
        this.image = image;
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public int getImage() {
        return image;
    }

    public float getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }
}

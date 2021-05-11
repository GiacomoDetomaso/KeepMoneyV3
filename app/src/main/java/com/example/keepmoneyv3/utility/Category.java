package com.example.keepmoneyv3.utility;

import java.io.Serializable;

/**
 * A class that identifies a category
 *
 * @author Michelangelo De Pascale
 * */

public class Category implements Serializable {
    private String id, name;
    private int pictureId;

    public Category(String id, String name, int pictureId){
        this.id = id;
        this.name = name;
        this.pictureId = pictureId;
    }

    public int getPictureId() {
        return pictureId;
    }

    public String getId() {
        return id;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

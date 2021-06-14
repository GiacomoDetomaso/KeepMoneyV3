package com.labproject.keepmoneyv3.utility;

import java.io.Serializable;

public class WishLists implements Serializable {
    private int id;
    private String name;
    private String description;
    private int isValid;

    public WishLists(int id, String name, String description, int isValid) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isValid = isValid;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}

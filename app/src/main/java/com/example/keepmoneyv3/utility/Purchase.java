package com.example.keepmoneyv3.utility;


import java.io.Serializable;

public class Purchase implements Serializable {
    private String time;
    private int itemId,id,listId;
    private String date;
    private String username;
    //Category tipo;


    public Purchase(int id, String date, String time, String username, int item, int listId) {
        this.id = id;
        this.itemId = item;
        this.time = time;
        this.date = date;
        this.username = username;
        this.listId = listId;
    }

    public Purchase(int id, int itemId, int listId, String username) {
        this.itemId = itemId;
        this.id = id;
        this.listId = listId;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public int getItemId() {
        return itemId;
    }

    public int getListId() {
        return listId;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

}
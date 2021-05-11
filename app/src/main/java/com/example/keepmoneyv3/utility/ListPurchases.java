package com.example.keepmoneyv3.utility;

import java.io.Serializable;

public class ListPurchases implements Serializable {
    private String descrizione;
    private int image;
    private float prezzo;

    public ListPurchases(String descrizione, int image,float prezzo) {
        this.descrizione = descrizione;
        this.image = image;
        this.prezzo = prezzo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public float getPrezzo() {
        return prezzo;
    }
}

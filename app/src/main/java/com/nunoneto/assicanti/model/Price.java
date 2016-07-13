package com.nunoneto.assicanti.model;

import io.realm.RealmObject;

/**
 * Created by NB20301 on 13/07/2016.
 */
public class Price extends RealmObject {

    private String type;
    private double price;

    public Price() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

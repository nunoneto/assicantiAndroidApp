package com.nunoneto.assicanti.model.entity;

/**
 * Created by NB20301 on 28/07/2016.
 */
public class SendOrderCodes {

    private String hash, cod;

    public SendOrderCodes(String hash, String cod) {
        this.hash = hash;
        this.cod = cod;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }
}

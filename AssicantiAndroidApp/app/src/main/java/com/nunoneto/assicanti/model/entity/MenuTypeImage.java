package com.nunoneto.assicanti.model.entity;

import io.realm.RealmObject;

/**
 * Created by NB20301 on 13/07/2016.
 */
public class MenuTypeImage extends RealmObject {

    private String menuType;
    private byte[] image;


    public MenuTypeImage() {
    }

    public MenuTypeImage(String menuType, byte[] image) {
        this.menuType = menuType;
        this.image = image;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

package com.nunoneto.assicanti.model;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by NB20301 on 12/07/2016.
 */
public class DayMenu extends RealmObject {

    private int dayOfWeek; // Calendar.MONDAY to Calendar.FRIDAY
    private String description;

    public DayMenu(){}

    public DayMenu(int dayOfWeek, String description){
        this.dayOfWeek = dayOfWeek;
        this.description = description;
    }


}

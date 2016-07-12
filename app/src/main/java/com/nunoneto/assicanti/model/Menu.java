package com.nunoneto.assicanti.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by NB20301 on 12/07/2016.
 */
public class Menu extends RealmObject {

    private Date starting, ending;
    private String type;
    private RealmList<DayMenu> days;

    public Menu(){
        this.days = new RealmList<>();
    }

    public Menu(Date starting, Date ending, String type){
        this.starting = starting;
        this.ending = ending;
        this.type = type;
        this.days = new RealmList<>();
    }

    public Date getStarting() {
        return starting;
    }

    public Date getEnding() {
        return ending;
    }

    public String getType() {
        return type;
    }

    public RealmList<DayMenu> getDays() {
        return days;
    }
}

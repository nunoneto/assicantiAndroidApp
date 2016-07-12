package com.nunoneto.assicanti.model;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by NB20301 on 12/07/2016.
 */
public class DayMenu extends RealmObject {

    private int dayOfWeek; // Calendar.MONDAY to Calendar.FRIDAY
    private String description;
    private String type;
    private RealmList<DayMenu> days;

    public DayMenu(){this.days = new RealmList<>();}

    public DayMenu(int dayOfWeek, String description, String type){
        this.dayOfWeek = dayOfWeek;
        this.description = description;
    }

    public RealmList<DayMenu> getDays() {
        return days;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDays(RealmList<DayMenu> days) {
        this.days = days;
    }
}

package com.nunoneto.assicanti.model;

import android.util.Base64;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by NB20301 on 12/07/2016.
 *
 *      WeekMenu
 *      |--MenuType
 *      |----|DayMenu
 *      |----|DayMenu
 *      |----|DayMenu
 *      |--MenuType
 *      |----|DayMenu
 *      |----|DayMenu
 *      |----|DayMenu
 */
public class WeekMenu extends RealmObject {

    @PrimaryKey
    private String menuId;
    private Date starting, ending;
    private RealmList<MenuType> types = new RealmList<>();

    public WeekMenu() {

    }

    public WeekMenu(Date starting, Date ending, String type) {
        this.starting = starting;
        this.ending = ending;
    }

    public void setMenuId(){
        this.menuId = new String(starting.getTime() + ending.getTime() + "");
    }

    public void setStarting(Date starting) {
        this.starting = starting;
    }

    public void setEnding(Date ending) {
        this.ending = ending;
    }

    public RealmList<MenuType> getTypes() {
        return types;
    }

    public void setTypes(RealmList<MenuType> types) {
        this.types = types;
    }
}
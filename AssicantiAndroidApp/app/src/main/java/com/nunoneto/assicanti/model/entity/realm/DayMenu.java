package com.nunoneto.assicanti.model.entity.realm;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by NB20301 on 12/07/2016.
 */
public class DayMenu extends RealmObject {

    private int dayOfWeek; // Calendar.MONDAY to Calendar.FRIDAY
    private String description;
    private String type;
    private RealmList<DayMenu> days;
    private MenuType menuType;

    public DayMenu(){this.days = new RealmList<>();}

    public DayMenu(int dayOfWeek, String description, String type){
        this.dayOfWeek = dayOfWeek;
        this.description = description;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RealmList<DayMenu> getDays() {
        return days;
    }

    public void setDays(RealmList<DayMenu> days) {
        this.days = days;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public void setMenuType(MenuType menuType) {
        this.menuType = menuType;
    }
}

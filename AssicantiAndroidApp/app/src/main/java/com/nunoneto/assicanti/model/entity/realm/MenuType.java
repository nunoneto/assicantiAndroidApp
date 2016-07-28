package com.nunoneto.assicanti.model.entity.realm;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Nuno on 12/07/2016.
 */
public class MenuType extends RealmObject{

    private String type; //meat, fish, ...
    private RealmList<DayMenu> days = new RealmList<>();
    private MenuTypeImage menuTypeImage;
    private RealmList<Price> prices = new RealmList<>();
    private WeekMenu weekMenu;

    public MenuType(){}

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

    public MenuTypeImage getMenuTypeImage() {
        return menuTypeImage;
    }

    public void setMenuTypeImage(MenuTypeImage menuTypeImage) {
        this.menuTypeImage = menuTypeImage;
    }

    public RealmList<Price> getPrices() {
        return prices;
    }

    public void setPrices(RealmList<Price> prices) {
        this.prices = prices;
    }

    public WeekMenu getWeekMenu() {
        return weekMenu;
    }

    public void setWeekMenu(WeekMenu weekMenu) {
        this.weekMenu = weekMenu;
    }
}

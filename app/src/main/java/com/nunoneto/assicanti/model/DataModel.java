package com.nunoneto.assicanti.model;

import java.util.List;

/**
 * Created by Nuno on 12/07/2016.
 */
public class DataModel {

    private static DataModel instance;

    private List<WeekMenu> menus;
    private WeekMenu currentMenu;

    public static DataModel getInstance() {
        return instance != null ? instance : new DataModel();
    }

    public List<WeekMenu> getMenus() {
        return menus;
    }

    public void setMenus(List<WeekMenu> menus) {
        this.menus = menus;
    }

    public WeekMenu getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(WeekMenu currentMenu) {
        this.currentMenu = currentMenu;
    }
}

package com.nunoneto.assicanti.model;

import com.nunoneto.assicanti.Utils;
import com.nunoneto.assicanti.model.entity.realm.DayMenu;
import com.nunoneto.assicanti.model.entity.realm.MenuType;
import com.nunoneto.assicanti.model.entity.realm.OptionalGroup;
import com.nunoneto.assicanti.model.entity.realm.Order;
import com.nunoneto.assicanti.model.entity.realm.WeekMenu;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Nuno on 12/07/2016.
 */
public class DataModel {

    private static DataModel instance;

    private final static int LIMIT_HOUR = 14;

    private List<WeekMenu> menus;
    private List<OptionalGroup> optionalGroups;
    private Order currentOrder;

    public static DataModel getInstance() {
        return instance != null ? instance : (instance = new DataModel());
    }

    public WeekMenu getCurrentMenu() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<WeekMenu> results = getCurrentMenuQuery().findAll();
        WeekMenu menus = results != null && results.size() > 0 ? realm.copyFromRealm(results).get(0) : null;
        realm.close();
        return menus;
    }

    public void deleteCurrentMenu(){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        getCurrentMenuQuery().findAll().deleteFromRealm(0);
        realm.commitTransaction();
        realm.close();
    }

    public Date getTargetDate(){
        Calendar cal = Utils.getCalendar();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY
                || (dayOfWeek == Calendar.FRIDAY && hour > LIMIT_HOUR)) {
            cal.add(Calendar.WEEK_OF_MONTH,1);
            cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        }else if(hour > LIMIT_HOUR){
            cal.add(Calendar.DAY_OF_WEEK,1);
        }
        return cal.getTime();
    }

    public Date getStartDate(){
        Calendar cal = Utils.getCalendar();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY
                || (dayOfWeek == Calendar.FRIDAY && hour > LIMIT_HOUR)){

            int daysToAdd = 0;
            if(dayOfWeek == Calendar.FRIDAY)
                daysToAdd = 2;
            else if(dayOfWeek == Calendar.SATURDAY)
                daysToAdd = 1;

            cal.add(Calendar.DAY_OF_YEAR, daysToAdd);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND,0);
            return cal.getTime();
        }else {
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            cal.add(Calendar.DAY_OF_YEAR, -1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            return cal.getTime();
        }
    }
    public Date getEndDate(){
        Calendar cal = Utils.getCalendar();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY
                || (dayOfWeek == Calendar.FRIDAY && hour > LIMIT_HOUR)){

            int daysToAdd = 0;
            if(dayOfWeek == Calendar.FRIDAY)
                daysToAdd = 2;
            else if(dayOfWeek == Calendar.SATURDAY)
                daysToAdd = 1;

            cal.add(Calendar.DAY_OF_YEAR, daysToAdd);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);

            cal.add(Calendar.DAY_OF_YEAR, 1);
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            return cal.getTime();

        }else{
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            cal.add(Calendar.DAY_OF_YEAR, -1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND,0);

            cal.add(Calendar.DAY_OF_YEAR, 1);
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            return cal.getTime();
        }
    }


    /**
     * Gets the most appropriate menu
     * If in a work day and before LIMIT_HOUR, the current day menu must be returned
     * if in a work day and after LIMIT_HOUR, the next day's menu must be returned
     * If in weekend or friday after LIMIT_HOUR, the next monday's menu must be returned, if available
     * @return
     */
    public HashMap<MenuType,DayMenu> getCurrentDayMenu(){
        HashMap<MenuType,DayMenu> dayMenuList = new HashMap<>();
        Calendar cal = Utils.getCalendar();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        Date startDate = getStartDate(),
                endDate = getEndDate();

        Realm realm = Realm.getDefaultInstance();
        RealmResults<WeekMenu> res = realm.where(WeekMenu.class)
                .greaterThanOrEqualTo("starting", startDate)
                .lessThanOrEqualTo("ending",endDate).findAll();

        if(res != null && res.size() > 0){
            WeekMenu weekMenu = res.get(0);
            if(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY
                    || (dayOfWeek == Calendar.FRIDAY && hour > LIMIT_HOUR)) {

                for(MenuType menuType : weekMenu.getTypes()){
                    for(DayMenu menu : menuType.getDays()){
                        if(menu.getDayOfWeek() == Calendar.MONDAY)
                            dayMenuList.put(menuType,menu);
                    }
                }
            }else{
                if(hour <= LIMIT_HOUR){
                    for(MenuType menuType : weekMenu.getTypes()){
                        for(DayMenu menu : menuType.getDays()) {
                            if(menu.getDayOfWeek() == Utils.getCalendar().get(Calendar.DAY_OF_WEEK))
                                dayMenuList.put(menuType,menu);
                        }
                    }
                }else{
                    for(MenuType menuType : weekMenu.getTypes()){
                        Calendar tempCal = Utils.getCalendar();
                        tempCal.add(Calendar.DAY_OF_WEEK, 1);
                        for(DayMenu menu : menuType.getDays()) {
                            if(menu.getDayOfWeek() == tempCal.get(Calendar.DAY_OF_WEEK))
                                dayMenuList.put(menuType,menu);
                        }
                    }

                }
            }
        }
        return dayMenuList;
    }



    private RealmQuery<WeekMenu> getCurrentMenuQuery(){
        Calendar cal = Utils.getCalendar();

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND,0);
        Date startDate = cal.getTime();

        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        Date endDate = cal.getTime();

        Realm realm = Realm.getDefaultInstance();
        return realm.where(WeekMenu.class)
                .greaterThanOrEqualTo("starting",startDate)
                .lessThanOrEqualTo("ending",endDate);
    }

    public WeekMenu getLastestWeekMenu(){

        Realm realm = Realm.getDefaultInstance();
        RealmResults<WeekMenu> res = realm.where(WeekMenu.class)
                .findAllSorted("starting", Sort.DESCENDING);
        return res != null && res.size() > 0 ? res.first() : null;
    }

    public List<OptionalGroup> getOptionalGroups() {
        return optionalGroups;
    }

    public void setOptionalGroups(List<OptionalGroup> optionalGroups) {
        this.optionalGroups = optionalGroups;
    }

    public static void setInstance(DataModel instance) {
        DataModel.instance = instance;
    }

    public List<WeekMenu> getMenus() {
        return menus;
    }

    public void setMenus(List<WeekMenu> menus) {
        this.menus = menus;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }
}

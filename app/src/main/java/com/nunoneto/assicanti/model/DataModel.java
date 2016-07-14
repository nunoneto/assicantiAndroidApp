package com.nunoneto.assicanti.model;

import com.nunoneto.assicanti.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Nuno on 12/07/2016.
 */
public class DataModel {

    private static DataModel instance;

    private List<WeekMenu> menus;
    private List<OptionalGroup> optionalGroups;



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

    private RealmQuery<WeekMenu> getCurrentMenuQuery(){
        Calendar cal = Utils.getCalendar();

        cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        cal.add(Calendar.DAY_OF_YEAR,-1);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        Date startDate = cal.getTime();

        cal.add(Calendar.DAY_OF_YEAR,1);
        cal.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
        Date endDate = cal.getTime();

        Realm realm = Realm.getDefaultInstance();
        return realm.where(WeekMenu.class)
                .greaterThanOrEqualTo("starting",startDate)
                .lessThanOrEqualTo("ending",endDate);
    }

    public List<OptionalGroup> getOptionalGroups() {
        return optionalGroups;
    }

    public void setOptionalGroups(List<OptionalGroup> optionalGroups) {
        this.optionalGroups = optionalGroups;
    }
}

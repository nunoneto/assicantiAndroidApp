package com.nunoneto.assicanti.webscraper;

import android.util.Log;

import com.nunoneto.assicanti.model.DayMenu;
import com.nunoneto.assicanti.model.MenuType;
import com.nunoneto.assicanti.model.WeekMenu;
import com.nunoneto.assicanti.model.Type;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by NB20301 on 12/07/2016.
 */
public class WebScrapper {

    private static WebScrapper instance;
    private static final String TAG = "WEB_SCRAPPER";
    private static final int JSOUP_TIMEOUT = 30 * 1000;

    // Documents
    private Document menuPage;

    public static WebScrapper getInstance() {
        return instance != null ? instance : new WebScrapper();
    }


    private void getMenuPage(){
        try {
            menuPage = Jsoup.connect("http://assicanti.pt/menus/").timeout(JSOUP_TIMEOUT).get();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"Could not get menu page...");
        }
    }

    /**
     * Parses menu page in order to get the menu information
     */
    public WeekMenu getMenus(){
        WeekMenu weekMenu = null;

        //Check if already in realm
        weekMenu = getWeekMenusFromPersistence();
        if(weekMenu != null)
            return weekMenu;

        //Get document if not in memory
        if(menuPage == null)
            getMenuPage();

        Elements els = menuPage.select("div.so-panel.widget.widget_wppizza > article");

        // Iterate over each menu: veg, fish, meat, ..

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        Iterator<Element> it = els.iterator();
        weekMenu = null;
        while (it.hasNext()){

            Element el = it.next();

            //DOM queries
            String type = el.select("h2.wppizza-article-title").text().replace("Menu ", "");
            Element info = el.select("div.wppizza-article-info").first();
            Elements menus = info.select("p");
            Element date = info.select("p").first();
            Elements dates = date.select("strong,b");

            if(weekMenu == null){
                Date start = null,
                        end = null;
                // Parse Dates
                if(dates.size() >= 2){
                    start = parseDate(dates.get(0),dates.get(1));
                }
                if(dates.size() >= 4){
                    end = parseDate(dates.get(2),dates.get(3));
                }
                weekMenu = realm.createObject(WeekMenu.class);
                weekMenu.setStarting(start);
                weekMenu.setEnding(end);
                weekMenu.setMenuId();
            }

            MenuType menuType = realm.createObject(MenuType.class);
            menuType.setType(type);
            weekMenu.getTypes().add(menuType);

            // get each day menu
            if(type.equals(Type.FISH) || type.equals(Type.VEGAN)){
                parseMeatFish(menus, menuType, type);
            }else {
                Iterator<Element> menuIt = menus.iterator();
                while (menuIt.hasNext()){
                    Element menu = menuIt.next();

                    // ingore the first elements, it's only dates
                    // dealt with otherwhere
                    if(!menu.text().equals(date.text())){
                        int dayOfWeek = parseWeekDay(menu.select("p > strong").text());
                        menu.select("p").remove();
                        String description = menu.text();

                        DayMenu dayMenu = realm.createObject(DayMenu.class);
                        dayMenu.setDayOfWeek(dayOfWeek);
                        dayMenu.setDescription(description);
                        dayMenu.setType(type);
                        menuType.getDays().add(dayMenu);

                    }
                }
            }
        }

        realm.commitTransaction();
        realm.close();

        return weekMenu;
    }

    private WeekMenu getWeekMenusFromPersistence(){

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        cal.set(Calendar.HOUR,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        Date startDate = cal.getTime();

        cal.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
        cal.set(Calendar.HOUR,23);
        cal.set(Calendar.MINUTE,59);
        cal.set(Calendar.SECOND,59);
        Date endDate = cal.getTime();

        Realm realm = Realm.getDefaultInstance();
        RealmResults<WeekMenu> results = realm.where(WeekMenu.class)
                .equalTo("starting",startDate)
                .equalTo("ending",endDate)
                .findAll();
        WeekMenu menus = results != null && results.size() > 0 ? realm.copyFromRealm(results).get(0) : null;
        realm.close();
        return menus;
    }

    /**
     * Receives HTML elements and parses the date from them
     * @param day
     * @param month
     * @return
     */
    private Date parseDate(Element day, Element month ){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM",new Locale("pt","pt"));
        int currYear = cal.get(Calendar.YEAR);
        String date = day.text() + " " + month.text();
        try {
            cal.setTime(sdf.parse(date));
            cal.set(Calendar.YEAR,currYear);
            return cal.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG,"Could not parse start date");
        }
        return null;
    }


    /**
     * Parse text into Calendar day of week
     * @param weekDayText
     * @return Calendar.MONDAY - Calendar.FRIDAY
     */
    private int  parseWeekDay(String weekDayText){
        weekDayText = weekDayText.replace("•","").replace(":","").replace("&nbsp;","").trim();
        SimpleDateFormat dayFormat = new SimpleDateFormat("E", new Locale("pt","pt"));
        Date date = null;
        try {
            date = dayFormat.parse(weekDayText);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG,"Could not parse week day "+weekDayText);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * This deals with some messed up syntax in the html, the fish and vegan are special case
     * ohh well...
     * @return
     */
    private void parseMeatFish(Elements menu, MenuType menuType, String type){
        Realm realm = Realm.getDefaultInstance();

        Element firstDay = menu.select("p").get(1);
        int dayOfWeek = parseWeekDay(firstDay.select("p > strong").first().text());
        firstDay.select("p").remove();
        String description = menu.text();

        DayMenu dayMenu = realm.createObject(DayMenu.class);
        dayMenu.setDayOfWeek(dayOfWeek);
        dayMenu.setDescription(description);
        dayMenu.setType(type);
        menuType.getDays().add(dayMenu);

        String[] restOfDays = menu.select("p").get(2).html().split("<br>");
        for(String day : restOfDays){
            Document thisDay = Jsoup.parseBodyFragment(day);
            if(thisDay.select("strong").first() != null){
                String dayText = thisDay.select("strong").first().html();

                dayOfWeek = parseWeekDay(dayText);
                thisDay.select("p").remove();
                description = thisDay.text();

                dayMenu = realm.createObject(DayMenu.class);
                dayMenu.setDayOfWeek(dayOfWeek);
                dayMenu.setDescription(description);
                dayMenu.setType(type);
                menuType.getDays().add(dayMenu);
            }
        }

    }





}

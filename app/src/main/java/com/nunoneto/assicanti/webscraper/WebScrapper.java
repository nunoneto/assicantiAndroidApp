package com.nunoneto.assicanti.webscraper;

import android.util.Log;

import com.nunoneto.assicanti.model.DayMenu;
import com.nunoneto.assicanti.model.Menu;
import com.nunoneto.assicanti.model.MenuType;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
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
    public List<Menu> getMenus(){

        List<Menu> menusArray;

        menusArray = getWeekMenusFromPersistence();
        if(menusArray != null && menusArray.size() > 0)
            return menusArray;

        menusArray = new ArrayList<>();
        if(menuPage == null)
            getMenuPage();

        Elements els = menuPage.select("div.so-panel.widget.widget_wppizza > article");

        // Iterate over each menu: veg, fish, meat, ..
        Iterator<Element> it = els.iterator();
        while (it.hasNext()){
            Menu thisMenu;

            Element el = it.next();
            Date start = null,
                    end = null;

            String menuType = el.select("h2.wppizza-article-title").text().replace("Menu ","");
            Element info = el.select("div.wppizza-article-info").first();
            Elements menus = info.select("p");
            Element date = info.select("p").first();
            Elements dates = date.select("strong,b");

            // Parse Dates
            if(dates.size() >= 2){
                start = parseDate(dates.get(0),dates.get(1));
            }
            if(dates.size() >= 4){
                end = parseDate(dates.get(2),dates.get(3));
            }

            thisMenu = new Menu(start,end,menuType);

            // get each day menu
            if(menuType.equals(MenuType.FISH) || menuType.equals(MenuType.VEGAN)){
                thisMenu.getDays().addAll(parseMeatFish(menus));
            }else {
                Iterator<Element> menuIt = menus.iterator();
                while (menuIt.hasNext()){
                    Element menu = menuIt.next();

                    // ingore the first elements, its only dates
                    // dealt with otherwise
                    if(!menu.text().equals(date.text())){
                        int dayOfWeek = parseWeekDay(menu.select("p > strong").text());
                        menu.select("p").remove();
                        String description = menu.text();
                        thisMenu.getDays()
                                .add(new DayMenu(
                                        dayOfWeek,
                                        description
                                ));
                    }
                }
            }

            menusArray.add(thisMenu);
        }

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        menusArray = realm.copyToRealm(menusArray);
        realm.commitTransaction();
        realm.close();

        return menusArray;
    }

    private List<Menu> getWeekMenusFromPersistence(){

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        Date startDate = cal.getTime();
        cal.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
        Date endDate = cal.getTime();

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Menu> results = realm.where(Menu.class)
                .equalTo("starting",startDate)
                .equalTo("ending",endDate)
                .findAll();
        List<Menu> menus = results != null && results.size() > 0 ? realm.copyFromRealm(results) : null;
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
     * This deal with some messed up syntax in the html, the fish and vegan are special case
     * ohh well...
     * @return
     */
    private List<DayMenu> parseMeatFish(Elements menu){
        List<DayMenu> days = new ArrayList<>();

        Element firstDay = menu.select("p").get(1);
        int dayOfWeek = parseWeekDay(firstDay.select("p > strong").first().text());
        firstDay.select("p").remove();
        String description = menu.text();

        DayMenu first = new DayMenu(dayOfWeek,description);
        days.add(first);

        String[] restOfDays = menu.select("p").get(2).html().split("<br>");
        for(String day : restOfDays){
            Document thisDay = Jsoup.parseBodyFragment(day);
            if(thisDay.select("strong").first() != null){
                String dayText = thisDay.select("strong").first().html();

                dayOfWeek = parseWeekDay(dayText);
                thisDay.select("p").remove();
                description = thisDay.text();

                DayMenu thisDayObj = new DayMenu(dayOfWeek,description);
                days.add(thisDayObj);
            }
        }

        return days;
    }





}

package com.nunoneto.assicanti.webscraper;

import android.util.Log;

import com.nunoneto.assicanti.model.DataModel;
import com.nunoneto.assicanti.model.entity.realm.DayMenu;
import com.nunoneto.assicanti.model.entity.realm.MenuType;
import com.nunoneto.assicanti.model.entity.realm.MenuTypeImage;
import com.nunoneto.assicanti.model.entity.realm.OptionalGroup;
import com.nunoneto.assicanti.model.entity.realm.OptionalItem;
import com.nunoneto.assicanti.model.entity.realm.Price;
import com.nunoneto.assicanti.model.entity.SendOrderCodes;
import com.nunoneto.assicanti.model.entity.SendOrderResult;
import com.nunoneto.assicanti.model.entity.realm.WeekMenu;
import com.nunoneto.assicanti.model.entity.Type;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
        return instance != null ? instance : (instance = new WebScrapper());
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
    public void getMenus(boolean forceUpdate){
        WeekMenu weekMenu = null;

        if(forceUpdate){
            DataModel.getInstance().deleteCurrentMenu();
        }else{
            //Check if menu already in realm
            HashMap<MenuType, DayMenu> dayMenuList = DataModel.getInstance().getCurrentDayMenu();
            if(dayMenuList != null && dayMenuList.size() > 0)
                return;
        }

        WeekMenu latestWeekMenu = DataModel.getInstance().getLastestWeekMenu();

        //Get document
        getMenuPage();

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        try{
            Elements els = menuPage.select("div.so-panel.widget.widget_wppizza > article");

            // Iterate over each menu: veg, fish, meat, ..
            Iterator<Element> it = els.iterator();
            weekMenu = null;
            boolean alreadyExists = false;
            while (it.hasNext() && !alreadyExists){

                Element el = it.next();

                //DOM queries
                String type = el.select("h2.wppizza-article-title").text().replace("Menu ", "");
                Element info = el.select("div.wppizza-article-info").first();
                Elements menus = info.select("p");
                Element date = info.select("p").first();
                Elements dates = date.select("strong,b");
                Elements prices = el.select(".wppizza-article-tiers > .wppizza-article-price");

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
                    if(latestWeekMenu != null && latestWeekMenu.getMenuId().equals(start.getTime()+end.getTime()+"")){
                        alreadyExists = true;
                        continue;
                    }

                    weekMenu = realm.createObject(WeekMenu.class);
                    weekMenu.setStarting(start);
                    weekMenu.setEnding(end);
                    weekMenu.setMenuId();
                }

                MenuType menuType = realm.createObject(MenuType.class);
                menuType.setType(type);
                weekMenu.getTypes().add(menuType);
                MenuTypeImage menuTypeImage = imageExists(type);
                if(menuTypeImage == null){
                    menuTypeImage = realm.createObject(MenuTypeImage.class);
                    menuTypeImage.setMenuType(type.toUpperCase());

                    String src = el.select(".wppizza-article-img > img").first().absUrl("src");;
                    menuTypeImage.setImage(downloadImage(src));
                }
                menuType.setMenuTypeImage(menuTypeImage);
                //price
                for(Element priceEl : prices){
                    Price price = realm.createObject(Price.class);
                    Number num = null;
                    try {
                        num = NumberFormat.getInstance().parse(priceEl.select("span > span").first().text());
                        price.setPrice(num.doubleValue());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    price.setType(priceEl.select(".wppizza-article-price-lbl").first().text());
                    price.setCurrency(priceEl.parent().select(".wppizza-article-price-currency").first().text().trim());
                    String[] id = priceEl.id().split("-");
                    price.setItemId(id[1]);
                    price.setTier(id[2]);
                    price.setSize(id[3]);
                    price.setId(priceEl.id());
                    menuType.getPrices().add(price);
                }
                // get each day menu
                if(type.equals(Type.FISH) || type.equals(Type.VEGAN)){
                    parseMeatFish(menus, menuType, type);
                }else {
                    Iterator<Element> menuIt = menus.iterator();
                    while  // ingore the first elements, it's only dates
                            (menuIt.hasNext()){
                        Element menu = menuIt.next();

                        // dealt with otherwhere
                        if(!menu.text().equals(date.text())){
                            int dayOfWeek = parseWeekDay(menu.select("p > strong").text());
                            menu.select("p").remove();
                            menu.select("strong").remove();
                            String description = menu.select("p").text().replace("• ","");

                            DayMenu dayMenu = realm.createObject(DayMenu.class);
                            dayMenu.setDayOfWeek(dayOfWeek);
                            dayMenu.setDescription(description);
                            dayMenu.setType(type);
                            menuType.getDays().add(dayMenu);

                        }
                    }
                }
            }
        }catch (Exception ex){
            Log.e(TAG,"Failed to parse menu page");
            ex.printStackTrace();
        }

        realm.commitTransaction();
        realm.close();
    }

    public List<OptionalGroup> parseOptionals(String html){

        List<OptionalGroup> groups = new ArrayList<>();
        try{
            Document doc = Jsoup.parse(html);
            Elements optGroups = doc.select(".wppizza-imulti > fieldset.wppizza-list-ingredients");

            String multiType = doc.select("#wppizza-ingr-multitype").first().attr("value");

            for(Element opt : optGroups){

                OptionalGroup group = new OptionalGroup();
                group.setName(opt.select("legend").first().text());
                group.setMultiType(multiType);

                Elements items = opt.select("ul > li");
                for (Element item : items){

                    String[] id = item.id().split("-");
                    Elements price = item.select(".wppizza-doingredient-price");

                    item.select("label.wppizza-doingredient-lbl > .wppizza-doingredient-price").remove();
                    OptionalItem optional = new OptionalItem(
                            item.select("label.wppizza-doingredient-lbl").first().text(),
                            id[3],
                            id[5],
                            id[2],
                            id[4],
                            price != null && price.size() > 0 ? price.first().text() : ""
                    );
                    group.getItems().add(optional);

                }
                groups.add(group);
            }
        }catch (Exception e){
            Log.e(TAG,"Could not parse optionals");
            e.printStackTrace();
        }
        return groups;
    }

    private byte[] downloadImage(String src){
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            URL url = new URL(src);
            is = url.openStream();
            baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;
            while((length = is.read(buffer))>0){
                baos.write(buffer,0,length);
            }
            return baos.toByteArray();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(baos != null){
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.e(TAG,"Couldnt read image from url "+src);
        return null;

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
        weekDayText = weekDayText.replace("•","").replace(":","").replace("&nbsp;","").replace(String.valueOf((char) 160),"").trim();
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
        firstDay.select("strong").remove();
        String description = firstDay.select("p").text().replace("• ","");

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
                thisDay.select("strong").remove();
                description = thisDay.text().replace("• ","");

                dayMenu = realm.createObject(DayMenu.class);
                dayMenu.setDayOfWeek(dayOfWeek);
                dayMenu.setDescription(description);
                dayMenu.setType(type);
                menuType.getDays().add(dayMenu);
            }
        }

    }

    private MenuTypeImage imageExists(String menuType){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<MenuTypeImage> images = realm.where(MenuTypeImage.class)
                .equalTo("menuType",menuType.toUpperCase())
                .findAll();
        return images != null && images.size() > 0 ? images.get(0) : null;

    }


    public String parseRegisterOrder(String html){
        try{
            Document doc = Jsoup.parse(html);

            String hash = doc.select("input").first().attr("value");

            return hash;
        }catch (Exception e){
            Log.e(TAG,"Could not parse send order page");
            e.printStackTrace();
        }
        return "";

    }

    public SendOrderResult parseSendOrder(String sendOrderHtml){

        try{
            Document doc = Jsoup.parse(sendOrderHtml);

            return new SendOrderResult(
                    doc.select("p:nth-of-type(1) > strong ").first().text(),
                    doc.select("p:nth-of-type(2) > strong ").first().text()
            );

        }catch (Exception e){
            Log.e(TAG,"Could not parse send order page");
            e.printStackTrace();
        }
        return null;
    }

    public SendOrderCodes getSendOrderHash(){
        try{
            Document doc = Jsoup.connect("http://assicanti.pt/finalizar-encomenda/").timeout(JSOUP_TIMEOUT).get();

            return new SendOrderCodes(
                doc.select("input#wppizza_hash").first().val(),
                doc.select("input#wppizza-gateway-cod").first().val()
            );

        }catch (Exception e){
            Log.e(TAG,"Could not get order hash");
            e.printStackTrace();
        }
            return null;
    }




}

package com.nunoneto.assicanti.model.entity;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by NB20301 on 25/07/2016.
 */
public class Order extends RealmObject{

    private String orderId;
    private WeekMenu weekMenu;
    private MenuType menuType;
    private DayMenu dayMenu;
    private Price price;
    private RealmList<OptionalItem> optionalItems = new RealmList<>();
    private CustomerData customerData;

    public Order() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public WeekMenu getWeekMenu() {
        return weekMenu;
    }

    public void setWeekMenu(WeekMenu weekMenu) {
        this.weekMenu = weekMenu;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public void setMenuType(MenuType menuType) {
        this.menuType = menuType;
    }

    public DayMenu getDayMenu() {
        return dayMenu;
    }

    public void setDayMenu(DayMenu dayMenu) {
        this.dayMenu = dayMenu;
    }

    public RealmList<OptionalItem> getOptionalItems() {
        return optionalItems;
    }

    public void setOptionalItems(RealmList<OptionalItem> optionalItems) {
        this.optionalItems = optionalItems;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public CustomerData getCustomerData() {
        return customerData;
    }

    public void setCustomerData(CustomerData customerData) {
        this.customerData = customerData;
    }
}

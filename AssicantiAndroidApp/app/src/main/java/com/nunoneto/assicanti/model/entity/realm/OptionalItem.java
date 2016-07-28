package com.nunoneto.assicanti.model.entity.realm;

import io.realm.RealmObject;

/**
 * Created by NB20301 on 14/07/2016.
 */
public class OptionalItem extends RealmObject {

    private String name;
    private String itemId;
    private String groupId;
    private String multiId;
    private String multiType;
    private String price;

    public OptionalItem() {
    }

    public OptionalItem(String name, String itemId, String groupId, String multiId, String multiType, String price) {
        this.name = name;
        this.itemId = itemId;
        this.groupId = groupId;
        this.multiId = multiId;
        this.multiType = multiType;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMultiId() {
        return multiId;
    }

    public void setMultiId(String multiId) {
        this.multiId = multiId;
    }

    public String getMultiType() {
        return multiType;
    }

    public void setMultiType(String multiType) {
        this.multiType = multiType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

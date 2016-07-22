package com.nunoneto.assicanti.model;

/**
 * Created by NB20301 on 14/07/2016.
 */
public class OptionalItem {

    private String name;
    private String itemId;
    private String groupId;
    private String multiId;
    private String multiType;

    public OptionalItem(String name, String itemId, String groupId, String multiId, String multiType) {
        this.name = name;
        this.itemId = itemId;
        this.groupId = groupId;
        this.multiId = multiId;
        this.multiType = multiType;
    }

    public String getName() {
        return name;
    }

    public String getItemId() {
        return itemId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getMultiId() {
        return multiId;
    }

    public String getMultiType() {
        return multiType;
    }
}

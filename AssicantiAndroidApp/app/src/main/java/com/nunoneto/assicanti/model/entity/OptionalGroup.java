package com.nunoneto.assicanti.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NB20301 on 14/07/2016.
 */
public class OptionalGroup {

    private String name;
    private String multiType;
    private List<OptionalItem> items = new ArrayList<>();

    public OptionalGroup() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItems(List<OptionalItem> items) {
        this.items = items;
    }

    public List<OptionalItem> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    public String getMultiType() {
        return multiType;
    }

    public void setMultiType(String multiType) {
        this.multiType = multiType;
    }
}

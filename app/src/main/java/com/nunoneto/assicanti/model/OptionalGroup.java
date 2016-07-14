package com.nunoneto.assicanti.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NB20301 on 14/07/2016.
 */
public class OptionalGroup {

    private String name;
    private List<OptionalItem> items = new ArrayList<>();

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
}

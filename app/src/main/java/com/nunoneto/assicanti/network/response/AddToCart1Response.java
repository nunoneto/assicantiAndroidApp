package com.nunoneto.assicanti.network.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by NB20301 on 15/07/2016.
 */
public class AddToCart1Response {

    @SerializedName("hasingredients")
    private String hasIngredients;

    @SerializedName("id")
    private String id;

    @SerializedName("tier")
    private String tier;

    @SerializedName("size")
    private String size;

    public String getHasIngredients() {
        return hasIngredients;
    }

    public String getId() {
        return id;
    }

    public String getTier() {
        return tier;
    }

    public String getSize() {
        return size;
    }
}

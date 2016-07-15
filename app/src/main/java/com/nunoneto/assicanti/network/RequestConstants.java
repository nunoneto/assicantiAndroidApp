package com.nunoneto.assicanti.network;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.Query;

/**
 * Created by NB20301 on 14/07/2016.
 */
public class RequestConstants {

    public class GetIngredients{
        public static final String ACTION = "wppizza_ingredients_json";
        public static final String TYPE = "getingredients";
    }

    public class AddIngredients{
        public static final String ACTION = "wppizza_ingredients_json";
        public static final String TYPE = "addingredient";
    }

    public class RemoveIngredients{
        public static final String ACTION = "wppizza_ingredients_json";
        public static final String TYPE = "removeingredient";
    }

    public class AddToCart1{
        public static final String ACTION = "wppizza_ingredients_json";
        public static final String TYPE = "diy-to-cart";
    }

    public class AddToCart2{
        public static final String ACTION = "wppizza_json";
        public static final String TYPE = "refresh";
    }



}

package com.nunoneto.assicanti.network;

import android.util.Xml;

import com.google.gson.annotations.SerializedName;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import retrofit2.http.Query;

/**
 * Created by NB20301 on 14/07/2016.
 */
public class RequestConstants {

    public static class GetIngredients{
        public static final String ACTION = "wppizza_ingredients_json";
        public static final String TYPE = "getingredients";
    }

    public static class AddIngredients{
        public static final String ACTION = "wppizza_ingredients_json";
        public static final String TYPE = "addingredient";
    }

    public static class RemoveIngredients{
        public static final String ACTION = "wppizza_ingredients_json";
        public static final String TYPE = "removeingredient";
    }

    public static class AddToCart1{
        public static final String ACTION = "wppizza_ingredients_json";
        public static final String TYPE = "diy-to-cart";
    }

    public static class AddToCart2{
        public static final String ACTION = "wppizza_json";
        public static final String TYPE = "refresh";
    }

    public static class Register{
        public static final String ACTION = "wppizza_json";
        public static final String TYPE = "nonce";
        public static final String VAL = "register";
    }

    public static class CheckIfOpen{
        public static final String ACTION = "wppizza_json";
        public static final String TYPE = "checkifopen";
    }

    public static class SendOrder{
        public static final String ACTION = "wppizza_json";
        public static final String TYPE = "sendorder";

        public static String buildFormQuery(
                String name, String email, String address, String contact, String comments, String companyCode, String nif, String hash){
            StringBuilder sb = new StringBuilder();
            sb.append("cname="+name+"&");
            sb.append("cemail="+email+"&");
            sb.append("caddress="+address+"&");
            sb.append("ccomments="+contact+"&");
            sb.append("ccustom1="+comments+"&");
            sb.append("ccustom1="+companyCode+"&");
            sb.append("ccustom2="+nif+"&");
            sb.append("wppizza-gateway=cod&");
            sb.append("wppizza_hash="+hash);

            try {
                return URLEncoder.encode(sb.toString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return sb.toString();
            }
        }
    }



}

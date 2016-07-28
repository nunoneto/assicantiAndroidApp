package com.nunoneto.assicanti.network;

import com.nunoneto.assicanti.network.response.AddOptionalResponse;
import com.nunoneto.assicanti.network.response.AddToCart1Response;
import com.nunoneto.assicanti.network.response.AddToCart2Response;
import com.nunoneto.assicanti.network.response.GetOptionalsResponse;
import com.nunoneto.assicanti.tasks.GetOptionalsTask;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by NB20301 on 12/07/2016.
 */
public interface AssicantiService {

    @FormUrlEncoded
    @POST("/wp-admin/admin-ajax.php")
    Call<GetOptionalsResponse> getOptionals(
            @Field("action") String action,
            @Field("vars[type]") String type,
            @Field("vars[item]") String item,
            @Field("vars[tier]") String tier,
            @Field("vars[size]") String size
    );

    @FormUrlEncoded
    @POST("/wp-admin/admin-ajax.php")
    Call<AddOptionalResponse> addRemoveOptional(
            @Field("action") String action,
            @Field("vars[type]") String type,
            @Field("vars[item]") String item,
            @Field("vars[postId]") String postId,
            @Field("vars[multiId]") String multiId,
            @Field("vars[multiType]") String multiType
    );

    @FormUrlEncoded
    @POST("/wp-admin/admin-ajax.php")
    Call<AddToCart1Response> addToCart1(
            @Field("action") String action,
            @Field("vars[type]") String type,
            @Field("vars[multiType]") String multiType,
            @Field("vars[data]") String data,
            @Field("vars[catId]") String catId
    );

    @FormUrlEncoded
    @POST("/wp-admin/admin-ajax.php")
    Call<AddToCart2Response> addToCart2(
            @Field("action") String action,
            @Field("vars[type]") String type,
            @Field("vars[id]") String id,
            @Field("vars[itemCount]") String itemCount,
            @Field("vars[catId]") String catId
    );

    @FormUrlEncoded
    @POST("/wp-admin/admin-ajax.php")
    Call<String> register(
            @Field("action") String action,
            @Field("vars[type]") String type,
            @Field("vars[val]") String val
    );


    @FormUrlEncoded
    @POST("/wp-admin/admin-ajax.php")
    Call<String[]> checkifopen(
            @Field("action") String action,
            @Field("vars[type]") String type
    );


    @FormUrlEncoded
    @POST("/wp-admin/admin-ajax.php")
    Call<String> sendOrder(
            @Field("action") String action,
            @Field("vars[type]") String type,
            @Field("vars[data]") String data
    );


}

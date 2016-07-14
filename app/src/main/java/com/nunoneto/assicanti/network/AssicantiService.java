package com.nunoneto.assicanti.network;

import com.nunoneto.assicanti.network.response.GetOptionalsResponse;

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
    Call<GetOptionalsResponse> addOptional(
            @Field("action") String action,
            @Field("vars[type]") String type,
            @Field("vars[item]") String item,
            @Field("vars[tier]") String tier,
            @Field("vars[size]") String size
    );


}

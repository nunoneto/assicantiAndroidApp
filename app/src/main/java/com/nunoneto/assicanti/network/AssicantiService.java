package com.nunoneto.assicanti.network;

import com.nunoneto.assicanti.network.response.AddOptionalResponse;
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


}

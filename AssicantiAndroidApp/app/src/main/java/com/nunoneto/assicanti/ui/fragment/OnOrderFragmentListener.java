package com.nunoneto.assicanti.ui.fragment;

import com.nunoneto.assicanti.model.entity.realm.Price;

/**
 * Created by NB20301 on 15/07/2016.
 */
public interface OnOrderFragmentListener {

    void showOptionals(Price price);

    void showForm();

    void setTitle(String title);

    void goToSummary(String orderNumber, String deliveryDate);
}

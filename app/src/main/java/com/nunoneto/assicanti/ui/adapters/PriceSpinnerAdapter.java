package com.nunoneto.assicanti.ui.adapters;

import com.nunoneto.assicanti.R;
import com.nunoneto.assicanti.model.Price;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.Arrays;
import java.util.List;

/**
 * Created by NB20301 on 13/07/2016.
 */
public class PriceSpinnerAdapter extends ArrayAdapter<Price> {

    private Context context;
    private List<Price> prices;

    public PriceSpinnerAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
    }

    public PriceSpinnerAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        this.context = context;
    }

    public PriceSpinnerAdapter(Context context, int resource, Price[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.prices = Arrays.asList(objects);
    }

    public PriceSpinnerAdapter(Context context, int resource, int textViewResourceId, Price[] objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.prices = Arrays.asList(objects);
    }

    public PriceSpinnerAdapter(Context context, int resource, List<Price> objects) {
        super(context, resource, objects);
        this.context = context;
        this.prices = objects;
    }

    public PriceSpinnerAdapter(Context context, int resource, int textViewResourceId, List<Price> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.prices = objects;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v =  getCustomView(position, convertView, parent);
        return v;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = LayoutInflater.from(context).inflate(R.layout.spinner_price, parent, false);

        TextView price = (TextView) row.findViewById(R.id.price);
        TextView priceType = (TextView) row.findViewById(R.id.priceType);

        Price p = prices.get(position);

        price.setText(p.getPrice() + "");
        priceType.setText(p.getType());

        return row;
    }

}
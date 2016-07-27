package com.nunoneto.assicanti.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nunoneto.assicanti.R;
import com.nunoneto.assicanti.model.DataModel;
import com.nunoneto.assicanti.model.entity.DayMenu;
import com.nunoneto.assicanti.model.entity.OptionalItem;
import com.nunoneto.assicanti.model.entity.Price;

public class OrderSummaryFragment extends Fragment {

    public static final String TAG = "ORDERSUMMARY_FRAG";

    // Views
    private LinearLayout orderDetailContainer;
    private TextView orderNumber, deliveryDate, totalAmount;

    private OnOrderFragmentListener mListener;

    public OrderSummaryFragment() {
    }

    public static OrderSummaryFragment newInstance() {
        OrderSummaryFragment fragment = new OrderSummaryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_order_summary, container, false);
        orderDetailContainer = (LinearLayout) root.findViewById(R.id.orderDetailContainer);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LayoutInflater inf = LayoutInflater.from(getActivity().getApplicationContext());

        CardView meal = (CardView) inf.inflate(R.layout.view_order_item,null);
        DayMenu dayMenu = DataModel.getInstance().getCurrentOrder().getDayMenu();
        Price menuPrice = DataModel.getInstance().getCurrentOrder().getPrice();
        orderDetailContainer.addView(meal);

        ((TextView)meal.findViewById(R.id.itemName)).setText(dayMenu.getDescription()+"("+menuPrice.getSize()+")");
        ((TextView)meal.findViewById(R.id.itemValue)).setText(menuPrice.getPrice()+" "+menuPrice.getCurrency());

        if(DataModel.getInstance().getCurrentOrder() != null){
            for(OptionalItem item : DataModel.getInstance().getCurrentOrder().getOptionalItems()){

                CardView cardItem = (CardView) inf.inflate(R.layout.view_order_item,null);
                ((TextView)cardItem.findViewById(R.id.itemName)).setText(item.getName());
                ((TextView)cardItem.findViewById(R.id.itemValue)).setText(item.getPrice().isEmpty()? "gratuito" : item.getPrice() + "€");
                orderDetailContainer.addView(cardItem,orderDetailContainer.getChildCount());
            }
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOrderFragmentListener) {
            mListener = (OnOrderFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnOrderFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}

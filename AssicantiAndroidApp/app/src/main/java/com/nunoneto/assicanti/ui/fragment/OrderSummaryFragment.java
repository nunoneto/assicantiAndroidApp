package com.nunoneto.assicanti.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nunoneto.assicanti.R;
import com.nunoneto.assicanti.model.DataModel;
import com.nunoneto.assicanti.model.entity.realm.DayMenu;
import com.nunoneto.assicanti.model.entity.realm.OptionalItem;
import com.nunoneto.assicanti.model.entity.realm.Price;
import com.nunoneto.assicanti.ui.DayMenuActivity;

public class OrderSummaryFragment extends Fragment {

    public static final String TAG = "ORDERSUMMARY_FRAG";
    private final static String PARAM_ORDER = "PARAM_ORDER";
    private final static String PARAM_DELIVERY = "PARAM_DELIVERY";

    // Views
    private LinearLayout orderDetailContainer;
    private TextView orderNumberTxt, deliveryDateTxt, totalAmountTxt;
    private String orderNumber, deliveryDate;
    private AppCompatButton finishFlow;

    private OnOrderFragmentListener mListener;

    public OrderSummaryFragment() {
    }

    public static OrderSummaryFragment newInstance(String orderNumber, String deliveryDate) {
        OrderSummaryFragment fragment = new OrderSummaryFragment();
        Bundle b = new Bundle();
        b.putString(PARAM_ORDER,orderNumber);
        b.putString(PARAM_DELIVERY,deliveryDate);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            this.orderNumber = getArguments().getString(PARAM_ORDER);
            this.deliveryDate = getArguments().getString(PARAM_DELIVERY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_order_summary, container, false);
        orderDetailContainer = (LinearLayout) root.findViewById(R.id.orderDetailContainer);
        orderNumberTxt = (TextView) root.findViewById(R.id.orderInfo);
        deliveryDateTxt = (TextView) root.findViewById(R.id.deliveryDate);
        totalAmountTxt = (TextView) root.findViewById(R.id.totalAmount);

        deliveryDateTxt.setText(deliveryDate);
        orderNumberTxt.setText(orderNumber);

        finishFlow = (AppCompatButton)root.findViewById(R.id.finishFlow);
        finishFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  reset activity
                startActivity(
                        new Intent(getActivity(), DayMenuActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                );
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int totalAmount = 0;
        LayoutInflater inf = LayoutInflater.from(getActivity().getApplicationContext());

        CardView meal = (CardView) inf.inflate(R.layout.view_order_item,null);
        DayMenu dayMenu = DataModel.getInstance().getCurrentOrder().getDayMenu();
        Price menuPrice = DataModel.getInstance().getCurrentOrder().getPrice();
        orderDetailContainer.addView(meal);

        totalAmount += menuPrice.getPrice();

        ((TextView)meal.findViewById(R.id.itemName)).setText(dayMenu.getDescription()+"("+menuPrice.getSize()+")");
        ((TextView)meal.findViewById(R.id.itemValue)).setText(menuPrice.getPrice()+" "+menuPrice.getCurrency());

        if(DataModel.getInstance().getCurrentOrder() != null){
            for(OptionalItem item : DataModel.getInstance().getCurrentOrder().getOptionalItems()){

                CardView cardItem = (CardView) inf.inflate(R.layout.view_order_item,null);
                ((TextView)cardItem.findViewById(R.id.itemName)).setText(item.getName());
                ((TextView)cardItem.findViewById(R.id.itemValue)).setText(item.getPrice().isEmpty()? "gratuito" : item.getPrice() + "€");
                orderDetailContainer.addView(cardItem,orderDetailContainer.getChildCount());

                try{
                    totalAmount += Double.parseDouble(item.getPrice());
                }catch (NumberFormatException ex){
                    // probably free item, therefore no price..
                }

            }
        }

        totalAmountTxt.setText(totalAmount+"€");

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
